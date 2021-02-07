package com.bayamsell.keycloak.rest;

import lombok.RequiredArgsConstructor;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;

@RequiredArgsConstructor
public class MailChimpResourceProvider implements RealmResourceProvider {

    private final KeycloakSession session;

    @Override
    public Object getResource() {
        return new MailChimpResource(session);
    }

    @Override
    public void close() {

    }
}
