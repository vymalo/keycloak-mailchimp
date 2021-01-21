commands="embed-server --std-out=echo --server-config=standalone-ha.xml"
commands="$commands,/subsystem=keycloak-server/spi=eventsListener/:add"
commands="$commands,/subsystem=keycloak-server/spi=eventsListener/provider=mailchimp/:add(enabled=true)"

commands="$commands,/subsystem=keycloak-server/spi=eventsListener/provider=mailchimp:write-attribute(name=properties.API_KEY,value=\"$MAILCHIMP_API_KEY\")"
commands="$commands,/subsystem=keycloak-server/spi=eventsListener/provider=mailchimp:write-attribute(name=properties.LIST_ID,value=\"$MAILCHIMP_LIST_ID\")"
commands="$commands,/subsystem=keycloak-server/spi=eventsListener/provider=mailchimp:write-attribute(name=properties.LISTENED_EVENT_LIST,value=\"$MAILCHIMP_LISTENED_EVENT_LIST\")"

commands="$commands,stop-embedded-server"

$JBOSS_HOME/bin/jboss-cli.sh --commands="$commands"

# For dev only, this prints the standalone-ha.xml which is used by keycloak in docker mode
# cat $JBOSS_HOME/standalone/configuration/standalone-ha.xml
