package com.bayamsell.keycloak.rest;

import com.bayamsell.keycloak.mailchimp.MailChimpConfig;
import com.bayamsell.keycloak.mailchimp.MailChimpConfigService;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.keycloak.models.KeycloakSession;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class MailChimpResource {

    private final KeycloakSession session;

    public MailChimpResource(KeycloakSession session) {
        this.session = session;
    }

    @PUT
    @NoCache
    @Path("config")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateConfig(MailChimpConfig config) {
        checkRealmAdmin();

        var service = getService();
        config = service.addConfig(config);
        return Response.ok(config).build();
    }

    @GET
    @NoCache
    @Path("config")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConfig() {
        checkRealmAdmin();

        var service = getService();
        var found = service.getForRealm();
        if (found == null) {
            var config = new MailChimpConfig();
            found = service.addConfig(config);
        }
        return Response.ok(found).build();
    }

    private MailChimpConfigService getService() {
        return session.getProvider(MailChimpConfigService.class);
    }

    private void checkRealmAdmin() {
        // TODO Check role
    }

}
