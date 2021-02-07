package com.bayamsell.keycloak.mailchimp.impl;

import com.bayamsell.keycloak.jpa.MailChimpConfigModel;
import com.bayamsell.keycloak.mailchimp.MailChimpConfig;
import com.bayamsell.keycloak.mailchimp.MailChimpConfigService;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.utils.KeycloakModelUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class MailChimpConfigServiceImpl implements MailChimpConfigService {

    private final KeycloakSession session;

    public MailChimpConfigServiceImpl(KeycloakSession session) {
        this.session = session;

        if (getRealm() == null) {
            throw new IllegalStateException("The service cannot accept a session without a realm in its context.");
        }
    }

    private EntityManager getEntityManager() {
        return session.getProvider(JpaConnectionProvider.class).getEntityManager();
    }

    protected RealmModel getRealm() {
        return session.getContext().getRealm();
    }

    @Override
    public MailChimpConfig getForRealm() {
        try {
            return new MailChimpConfig(findOne());
        } catch (NoResultException e) {
            return null;
        }
    }

    private MailChimpConfigModel findOne() {
        TypedQuery<MailChimpConfigModel> namedQuery = getEntityManager()
                .createNamedQuery("findByRealm", MailChimpConfigModel.class)
                .setParameter("realmId", getRealm().getId());
        try {
            return namedQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public MailChimpConfig addConfig(MailChimpConfig config) {
        MailChimpConfigModel entity = new MailChimpConfigModel();
        if (config.getId() != null) {
            var found = findOne();
            if (found != null) {
                found.setListId(config.getListId());
                found.setApiKey(config.getApiKey());
                found.setListenedEvents(config.getListenedEvents());

                getEntityManager().persist(found);
                return config;
            }
        }

        String id = config.getId() == null ? KeycloakModelUtils.generateId() : config.getId();
        entity.setId(id);
        entity.setRealmId(getRealm().getId());

        entity.setApiKey(config.getApiKey());
        entity.setListId(config.getListId());
        entity.setListenedEvents(config.getListenedEvents());

        getEntityManager().persist(entity);
        config.setId(id);
        return config;
    }

    @Override
    public void deleteConfig() {
        var found = getForRealm();
        if (found != null) {
            getEntityManager().remove(found);
        }
    }

    @Override
    public void close() {

    }
}
