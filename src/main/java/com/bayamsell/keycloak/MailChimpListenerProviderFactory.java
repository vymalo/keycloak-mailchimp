package com.bayamsell.keycloak;

import org.apache.commons.lang.StringUtils;
import org.keycloak.Config;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.events.EventType;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ServerInfoAwareProviderFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MailChimpListenerProviderFactory implements EventListenerProviderFactory, ServerInfoAwareProviderFactory {

    private static final String ID = "mailchimp";
    private static final LinkedHashMap<String, String> SPI_INFO = new LinkedHashMap<>();

    static {
        SPI_INFO.put("provider_id", ID);
        SPI_INFO.put("mailchimp-bys-v", "1.0.3");
    }

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
        API_KEY = StringUtils.trimToEmpty(cKey);
        if (StringUtils.isBlank(API_KEY)) {
            throw new IllegalArgumentException("API_KEY cannot be empty");
        }

        String cList = config.get("LIST_ID");
        LIST_ID = StringUtils.trimToEmpty(cList);
        if (StringUtils.isBlank(LIST_ID)) {
            throw new IllegalArgumentException("LIST_ID cannot be empty");
        }

        String[] listenedEventLists = config.getArray("LISTENED_EVENT_LIST");
        if (listenedEventLists != null && listenedEventLists.length > 0) {
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
        return SPI_INFO;
    }
}
