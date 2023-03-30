package com.vymalo.keycloak.events;

import com.github.alexanderwe.bananaj.connection.MailChimpConnection;
import com.github.alexanderwe.bananaj.model.list.member.EmailType;
import com.github.alexanderwe.bananaj.model.list.member.Member;
import com.github.alexanderwe.bananaj.model.list.member.MemberStatus;
import com.vymalo.keycloak.mailchimp.MailChimpConfig;
import com.vymalo.keycloak.mailchimp.MailChimpConfigService;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MailChimpListenerProvider implements EventListenerProvider {

    private final MailChimpConnection con;
    private final String listId;
    private final Collection<EventType> listenedEvents;
    private final KeycloakSession keycloakSession;

    public MailChimpListenerProvider(KeycloakSession keycloakSession) {
        this.keycloakSession = keycloakSession;

        var service = getService();
        var found = service.getForRealm();
        if (found == null) {
            var config = new MailChimpConfig();
            found = service.addConfig(config);
        }

        if (found.getApiKey() != null && found.getListId() != null) {
            con = new MailChimpConnection(found.getApiKey());
            listenedEvents = found.getListenedEvents();
            listId = found.getListId();
        } else {
            con = null;
            listenedEvents = Collections.emptyList();
            listId = null;
        }
    }

    private boolean cannotRegister() {
        return this.con == null || this.listId == null;
    }

    private MailChimpConfigService getService() {
        return keycloakSession.getProvider(MailChimpConfigService.class);
    }

    @Override
    public void onEvent(Event event) {
        if (cannotRegister()) {
            return;
        }

        for (EventType listenedEvent : listenedEvents) {
            if (event.getType().equals(listenedEvent)) {
                final var context = keycloakSession.getContext();
                final var authenticationSession = context.getAuthenticationSession();
                final var authenticatedUser = authenticationSession.getAuthenticatedUser();

                try {
                    if (listenedEvent == EventType.DELETE_ACCOUNT) {
                        deregisterUser(authenticatedUser);
                    } else {
                        registerUser(authenticatedUser, event.getIpAddress());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void deregisterUser(UserModel userModel) throws Exception {
        final var mainList = con.getList(listId);
        final var byteOfMessage = userModel.getEmail().getBytes(StandardCharsets.UTF_8);
        final var md5 = MessageDigest.getInstance("MD5");
        final var digest = md5.digest(byteOfMessage);

        final var member = mainList.getMember(new String(digest));
        mainList.deleteMemberFromList(member.getId());
    }

    private void registerUser(UserModel userModel, String ipAddress) throws Exception {
        var mainList = con.getList(listId);
        var attributes = userModel.getAttributes();

        List<String> phone = attributes.get("phoneNumber");
        final var phoneNumber = phone != null && phone.size() > 0 ? phone.get(0) : null;

        List<String> locale = attributes.get("locale");
        final var language = locale != null && locale.size() > 0 ? locale.get(0) : null;

        List<String> photoUrl = attributes.get("photoUrl");
        final var avatarUrl = photoUrl != null && photoUrl.size() > 0 ? photoUrl.get(0) : null;

        List<String> birthdayList = attributes.get("birthday");
        final var birthday = birthdayList != null && birthdayList.size() > 0 ? birthdayList.get(0) : null;

        final var mergeFields = new HashMap<String, Object>();
        mergeFields.put("FNAME", userModel.getFirstName());
        mergeFields.put("LNAME", userModel.getLastName());
        mergeFields.put("PHONE", phoneNumber);
        mergeFields.put("LANGUAGE", language);
        mergeFields.put("AVATAR", avatarUrl);

        // The Date is in the format https://mailchimp.com/fr/help/change-a-date-field-to-a-birthday-field/
        mergeFields.put("BIRTHDAY", birthday(birthday));

        LocalDateTime timeStamp = LocalDateTime.now();
        HashMap<String, Boolean> memberInterest = new HashMap<>();

        var member = new Member.Builder()
                .emailAddress(userModel.getEmail())
                .language(language)
                .list(mainList)
                .emailType(EmailType.HTML)
                .status(MemberStatus.SUBSCRIBED)
                .mergeFields(mergeFields)
                .statusIfNew(MemberStatus.SUBSCRIBED)
                .ipSignup(ipAddress)
                .timestampSignup(timeStamp)
                .ipOpt(ipAddress)
                .timestampOpt(timeStamp)
                .memberInterest(memberInterest)
                .build();

        mainList.addOrUpdateMember(member);
    }

    private String birthday(String birthday) throws ParseException {
        if (birthday == null) return null;

        final var date = new SimpleDateFormat("dd/MM/yyyy").parse(birthday);

        if (date == null) return null;

        return String.format("%d/%d", date.getMonth() + 1, date.getDate());
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {
        if (cannotRegister()) {
        }
        // TODO SSE
    }

    @Override
    public void close() {
    }
}
