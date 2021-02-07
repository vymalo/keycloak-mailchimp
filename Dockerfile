ARG TAG=latest

FROM jboss/keycloak:${TAG}

ENV MAILCHIMP_PLUGIN_VERSION 1.0.3

LABEL maintainer="Stephane, Segning Lambou <selastlambou@gmail.com>"

RUN mkdir $JBOSS_HOME/providers

RUN curl -H "Accept: application/zip" https://github.com/bayamsell/keycloak-mailchimp/releases/download/v${MAILCHIMP_PLUGIN_VERSION}/keycloak-mailchimp-${MAILCHIMP_PLUGIN_VERSION}.jar -o $JBOSS_HOME/providers/keycloak-mailchimp-${MAILCHIMP_PLUGIN_VERSION}.jar -Li
