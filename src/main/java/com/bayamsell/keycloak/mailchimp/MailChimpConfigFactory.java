package com.bayamsell.keycloak.mailchimp;

import org.keycloak.provider.ProviderFactory;
import org.keycloak.provider.ServerInfoAwareProviderFactory;

public interface MailChimpConfigFactory
        extends ProviderFactory<MailChimpConfigService>, ServerInfoAwareProviderFactory {
}
