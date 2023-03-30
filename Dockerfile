ARG TAG=21.0.1

FROM quay.io/keycloak/keycloak:${TAG}

ENV MAILCHIMP_PLUGIN_VERSION 1.1.0
ENV KEYCLOAK_DIR /opt/keycloak
ENV KC_PROXY edge

LABEL maintainer="Stephane, Segning Lambou <selastlambou@gmail.com>"

USER 0

RUN mkdir $JBOSS_HOME/providers

RUN curl -H "Accept: application/zip" https://github.com/vymalo/keycloak-mailchimp/releases/download/v${MAILCHIMP_PLUGIN_VERSION}/keycloak-mailchimp-${MAILCHIMP_PLUGIN_VERSION}.jar -o $JBOSS_HOME/providers/keycloak-mailchimp-${MAILCHIMP_PLUGIN_VERSION}.jar -Li

RUN $KEYCLOAK_DIR/bin/kc.sh build

USER 1000
