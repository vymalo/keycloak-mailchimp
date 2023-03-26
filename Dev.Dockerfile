ARG TAG=latest

FROM jboss/keycloak:${TAG}

ENV MAILCHIMP_PLUGIN_VERSION 1.1.0

LABEL maintainer="Stephane, Segning Lambou <selastlambou@gmail.com>"

RUN mkdir $JBOSS_HOME/providers

COPY target/keycloak-mailchimp-${MAILCHIMP_PLUGIN_VERSION}.jar $JBOSS_HOME/providers/keycloak-mailchimp-${MAILCHIMP_PLUGIN_VERSION}.jar
