package com.vymalo.keycloak.events;

import org.keycloak.Config;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ServerInfoAwareProviderFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public class MailChimpListenerProviderFactory implements EventListenerProviderFactory, ServerInfoAwareProviderFactory {

    private static final String ID = "mailchimp-events";
    private static final LinkedHashMap<String, String> SPI_INFO = new LinkedHashMap<>();

    static {
        SPI_INFO.put("provider_id", ID);
        SPI_INFO.put(ID, "1.1.0");
    }

    @Override
    public MailChimpListenerProvider create(KeycloakSession keycloakSession) {
        return new MailChimpListenerProvider(keycloakSession);
    }

    @Override
    public void init(Config.Scope config) {
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
