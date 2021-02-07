package com.bayamsell.keycloak.jpa;

import org.keycloak.Config;
import org.keycloak.connections.jpa.entityprovider.JpaEntityProvider;
import org.keycloak.connections.jpa.entityprovider.JpaEntityProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ServerInfoAwareProviderFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public class MailChimpJpaEntityProviderFactory implements JpaEntityProviderFactory, ServerInfoAwareProviderFactory {

    public static final String ID = "mailchimp-jpa";
    private static final LinkedHashMap<String, String> SPI_INFO = new LinkedHashMap<>();

    static {
        SPI_INFO.put("provider_id", ID);
        SPI_INFO.put(ID, "1.0.0");
    }

    @Override
    public JpaEntityProvider create(KeycloakSession session) {
        return new MailChimpJpaEntityProvider();
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
