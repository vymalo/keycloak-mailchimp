package com.bayamsell.keycloak;

import org.keycloak.Config;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.events.EventType;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ServerInfoAwareProviderFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MailChimpListenerProviderFactory implements EventListenerProviderFactory, ServerInfoAwareProviderFactory {

    private static final String ID = "mailchimp";
    private String API_KEY;
    private String LIST_ID;
    private Collection<EventType> listenedEvents;

    @Override
    public MailChimpListenerProvider create(KeycloakSession keycloakSession) {
        return new MailChimpListenerProvider(API_KEY, LIST_ID, listenedEvents, keycloakSession);
    }

    @Override
    public void init(Config.Scope config) {
        String cKey = config.get("API_KEY");
        API_KEY = Objects.requireNonNull(cKey);

        String cList = config.get("LIST_ID");
        LIST_ID = Objects.requireNonNull(cList);

        String[] listenedEventLists = config.getArray("LISTENED_EVENT_LIST");
        if (listenedEventLists != null) {
            List<String> listenedEvents = Arrays.asList(listenedEventLists);
            Stream<EventType> eventTypeStream = listenedEvents.stream().map(EventType::valueOf);
            this.listenedEvents = eventTypeStream.collect(Collectors.toList());
        } else {
            listenedEvents = Arrays.asList(EventType.LOGIN, EventType.REGISTER);
        }
    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {
    }

    @Override
    public void close() {
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public Map<String, String> getOperationalInfo() {
        Map<String, String> ret = new LinkedHashMap<>();
        ret.put("provider_id", ID);
        ret.put("mailchimp-bys-v", "0.0.1");
        ret.put("api-key", "is set in config or in java code");
        return ret;
    }
}
