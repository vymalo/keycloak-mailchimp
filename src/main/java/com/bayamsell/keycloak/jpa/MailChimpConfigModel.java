package com.bayamsell.keycloak.jpa;

import lombok.Data;
import org.keycloak.events.EventType;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@Table(name = "MAILCHIMP_CONFIG")
@NamedQueries({@NamedQuery(name = "findByRealm", query = "from MailChimpConfigModel where realmId = :realmId")})
public class MailChimpConfigModel {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "REALM_ID", nullable = false, unique = true)
    private String realmId;

    @Column(name = "API_KEY", nullable = false)
    private String apiKey;

    @Column(name = "LIST_ID", nullable = false)
    private String listId;

    @Convert(converter = EventsConverter.class)
    @Column(name = "LISTENED_EVENTS", nullable = false)
    private Collection<EventType> listenedEvents;
}
