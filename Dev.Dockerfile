ARG TAG=latest

FROM jboss/keycloak:${TAG}

ENV MAILCHIMP_API_KEY someApi-Key
ENV MAILCHIMP_API_KEY someApiKey
ENV MAILCHIMP_LISTENED_EVENT_LIST "LOGIN,REGISTER,LOGOUT"

ENV MAILCHIMP_PLUGIN_VERSION 1.0.2

LABEL maintainer="Stephane, Segning Lambou <selastlambou@gmail.com>"

RUN mkdir $JBOSS_HOME/providers

COPY target/keycloak-mailchimp-${MAILCHIMP_PLUGIN_VERSION}.jar $JBOSS_HOME/providers/keycloak-mailchimp-${MAILCHIMP_PLUGIN_VERSION}.jar
COPY target/keycloak-mailchimp-${MAILCHIMP_PLUGIN_VERSION}-jar-with-dependencies.jar $JBOSS_HOME/providers/keycloak-mailchimp-${MAILCHIMP_PLUGIN_VERSION}-jar-with-dependencies.jar

COPY ./startup.sh /opt/jboss/startup-scripts/mailchimp-startup.sh

USER 0

RUN chmod +x /opt/jboss/startup-scripts/mailchimp-startup.sh

USER jboss

# Clean the fact that an embeded server did started
RUN rm -rf $JBOSS_HOME/standalone/configuration/standalone_xml_history/
