ARG TAG=latest

FROM jboss/keycloak:${TAG}

ENV MAILCHIMP_API_KEY someApiKey
ENV MAILCHIMP_API_KEY someApiKey

ENV MAILCHIMP_PLUGIN_VERSION 1.0.2

LABEL maintainer="Stephane, Segning Lambou <selastlambou@gmail.com>"

RUN mkdir $JBOSS_HOME/providers

RUN curl -H "Accept: application/zip" https://github.com/bayamsell/keycloak-mailchimp/releases/download/v${MAILCHIMP_PLUGIN_VERSION}/keycloak-mailchimp-${MAILCHIMP_PLUGIN_VERSION}.jar -o $JBOSS_HOME/providers/keycloak-mailchimp-${MAILCHIMP_PLUGIN_VERSION}.jar -Li
RUN curl -H "Accept: application/zip" https://github.com/bayamsell/keycloak-mailchimp/releases/download/v${MAILCHIMP_PLUGIN_VERSION}/keycloak-mailchimp-${MAILCHIMP_PLUGIN_VERSION}-jar-with-dependencies.jar -o $JBOSS_HOME/providers/keycloak-mailchimp-${MAILCHIMP_PLUGIN_VERSION}-jar-with-dependencies.jar -Li

COPY ./startup.sh /opt/jboss/startup-scripts/mailchimp-startup.sh

USER 0

RUN chmod +x /opt/jboss/startup-scripts/mailchimp-startup.sh

USER jboss

# Clean the fact that an embeded server did started
RUN rm -rf $JBOSS_HOME/standalone/configuration/standalone_xml_history/
