package com.vymalo.keycloak.jpa;

import org.keycloak.connections.jpa.entityprovider.JpaEntityProvider;

import java.util.List;

public class MailChimpJpaEntityProvider implements JpaEntityProvider {

    @Override
    public List<Class<?>> getEntities() {
        return List.of(MailChimpConfigModel.class);
    }

    @Override
    public String getChangelogLocation() {
        return "META-INF/mailchimp-config-changelog.xml";
    }

    @Override
    public String getFactoryId() {
        return MailChimpJpaEntityProviderFactory.ID;
    }

    @Override
    public void close() {

    }

}
