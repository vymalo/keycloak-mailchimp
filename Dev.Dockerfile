ARG TAG=21.0.1

FROM quay.io/keycloak/keycloak:${TAG}

ENV MAILCHIMP_PLUGIN_VERSION 1.1.0

ENV KEYCLOAK_DIR /opt/keycloak
ENV KC_PROXY edge

LABEL maintainer="Stephane, Segning Lambou <selastlambou@gmail.com>"

USER 0

COPY target/keycloak-mailchimp-${MAILCHIMP_PLUGIN_VERSION}.jar $KEYCLOAK_DIR/providers/keycloak-mailchimp.jar

RUN $KEYCLOAK_DIR/bin/kc.sh build

USER 1000
