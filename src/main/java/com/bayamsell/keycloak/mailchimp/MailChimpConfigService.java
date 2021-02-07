package com.bayamsell.keycloak.mailchimp;

import org.keycloak.provider.Provider;

public interface MailChimpConfigService extends Provider {

    MailChimpConfig getForRealm();

    MailChimpConfig addConfig(MailChimpConfig config);

    void deleteConfig();

}
