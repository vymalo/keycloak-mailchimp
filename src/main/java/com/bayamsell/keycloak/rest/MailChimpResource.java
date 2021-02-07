package com.bayamsell.keycloak.rest;

// import lombok.extern.jbosslog.JBossLog;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.keycloak.common.ClientConnection;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.managers.AuthenticationManager;
import org.keycloak.services.managers.RealmManager;
import org.keycloak.services.resources.admin.AdminAuth;
import org.keycloak.services.resources.admin.permissions.AdminPermissionEvaluator;
import org.keycloak.services.resources.admin.permissions.AdminPermissions;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

// @JBossLog
public class MailChimpResource {

    private final KeycloakSession session;

    private final AppAuthManager authManager = new AppAuthManager();

    private final AdminAuth auth;
    private final AdminPermissionEvaluator realmAuth;

    @Context
    private HttpHeaders httpHeaders;

    @Context
    private ClientConnection clientConnection;

    public MailChimpResource(KeycloakSession session) {
        this.session = session;
        var realm = session.getContext().getRealm();

        auth = authenticateRealmAdminRequest();

        var realmManager = new RealmManager(session);
        if (realm == null) {
            throw new NotFoundException("Realm not found.");
        }

        if (!auth.getRealm().equals(realmManager.getKeycloakAdminstrationRealm())
                && !auth.getRealm().equals(realm)) {
            throw new org.keycloak.services.ForbiddenException();
        }
        realmAuth = AdminPermissions.evaluator(session, realm, auth);

        session.getContext().setRealm(realm);
    }

    @PUT
    @NoCache
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateConfig(MultipartFormDataInput input) {
        try {
            if (auth == null) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            canManageUsers();

            // log.info("TODO: Do job here");

        } catch (ForbiddenException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (Exception e) {
            // log.error("error saving user avatar", e);
            return Response.serverError().entity(e.getMessage()).build();
        }

        return Response.ok().build();
    }

    private void canManageUsers() {
        if (!realmAuth.users().canManage()) {
            // log.info("user does not have permission to manage users");
            throw new ForbiddenException("user does not have permission to manage users");
        }
    }

    protected AdminAuth authenticateRealmAdminRequest() {
        var realm = session.getContext().getRealm();
        var client = session.getContext().getClient();

        AuthenticationManager.AuthResult authResult = authManager.authenticateIdentityCookie(session, realm);

        if (authResult == null) {
            throw new NotAuthorizedException("Bearer");
        }

        return new AdminAuth(realm, authResult.getToken(), authResult.getUser(), client);
    }
}
