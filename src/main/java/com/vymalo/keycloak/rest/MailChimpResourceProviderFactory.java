package com.vymalo.keycloak.rest;

import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ServerInfoAwareProviderFactory;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.services.resource.RealmResourceProviderFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public class MailChimpResourceProviderFactory
        implements RealmResourceProviderFactory, ServerInfoAwareProviderFactory {

    private static final String ID = "mailchimp-resource";
    private static final LinkedHashMap<String, String> SPI_INFO = new LinkedHashMap<>();

    static {
        SPI_INFO.put("provider_id", ID);
        SPI_INFO.put(ID, "1.1.0");
    }

    @Override
    public RealmResourceProvider create(KeycloakSession session) {
        return new MailChimpResourceProvider(session);
    }

    @Override
    public void init(Config.Scope config) {

    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {

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
