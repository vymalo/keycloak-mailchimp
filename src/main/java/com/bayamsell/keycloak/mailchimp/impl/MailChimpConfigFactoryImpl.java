package com.bayamsell.keycloak.mailchimp.impl;

import com.bayamsell.keycloak.mailchimp.MailChimpConfigFactory;
import com.bayamsell.keycloak.mailchimp.MailChimpConfigService;
import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public class MailChimpConfigFactoryImpl implements MailChimpConfigFactory {

    public static final String ID = "mailchimp-config-impl";
    private static final LinkedHashMap<String, String> SPI_INFO = new LinkedHashMap<>();

    static {
        SPI_INFO.put("provider_id", ID);
        SPI_INFO.put(ID, "1.0.0");
    }

    @Override
    public MailChimpConfigService create(KeycloakSession session) {
        return new MailChimpConfigServiceImpl(session);
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
