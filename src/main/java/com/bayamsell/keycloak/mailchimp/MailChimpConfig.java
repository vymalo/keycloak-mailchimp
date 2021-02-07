package com.bayamsell.keycloak.mailchimp;

import com.bayamsell.keycloak.jpa.MailChimpConfigModel;
import lombok.Data;
import org.keycloak.events.EventType;

import java.util.Collection;

@Data
public class MailChimpConfig {

    private String id;

    private String apiKey;

    private String listId;

    private Collection<EventType> listenedEvents;

    public MailChimpConfig(MailChimpConfigModel singleResult) {
        if (singleResult != null) {
            this.id = singleResult.getId();
            this.apiKey = singleResult.getApiKey();
            this.listId = singleResult.getListId();
            this.listenedEvents = singleResult.getListenedEvents();
        }
    }

    public MailChimpConfig() {
        this(null);
    }
}
