# Keycloak Mailchimp

As we're working with Keycloak, keep in mind that events managed by 
Keycloak are enum imported from `org.keycloak.events.EventType`.

## Manual config
A. First way

1. Add this inside the Keycloak `standalone*.xml` you're using:
    ```xml
    <spi name="eventsListener">
        <provider name="mailchimp-events" enabled="true">
            <properties>
                <property name="API_KEY" value="api-key"/>
                <property name="LIST_ID" value="api-value"/>
                <!-- (optionally: list of events) <property name="LISTENED_EVENT_LIST" value="[&quot;LOGIN&quot;,&quot;REGISTER&quot;,&quot;LOGOUT&quot;]"/> -->
            </properties>
        </provider>
    </spi>
    ```

2. Now, add `target/*.jar` files into `providers` folder of your keycloak.

3. Then start.

B. Second way

1. Now, add `target/*.jar` files into `providers` folder of your keycloak.

3. Then start.

## Auto config

### Docker

Simply use this preconfigured [docker-compose.yml](./docker-compose.yml), which will use the [Dockerfile](./Dockerfile).
You need to provide `MAILCHIMP_API_KEY` and `MAILCHIMP_LIST_ID` as environment variable.

1. docker-compose.yml: An example is the following
```yaml
version: "3.8"

services:

  keycloak:
    build: .
    ports:
      - 8080:8080
    environment:
      KEYCLOAK_USER: admin
      KEYCLOAK_PASSWORD: password
      MAILCHIMP_API_KEY: one
      MAILCHIMP_LIST_ID: two
      # MAILCHIMP_LISTENED_EVENT_LIST: LOGIN,REGISTER # optional <--
```

Here you could simply build using
```bash
docker-compose build
```

and then launch using
```
docker-compose up -d
```

2. Dockerfile: Dockerfile

- First build
   ```bash
   docker build -t vymalo/mailchimp .
   ```
- Then run
   ```bash
  docker run --rm -e MAILCHIMP_API_KEY=one -e MAILCHIMP_LIST_ID=two -p 8080:8080 vymalo/mailchimp
   ```

## Links

- https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/7.0/html/management_cli_guide/running_embedded_server
- https://stackoverflow.com/a/58144078/7748446
- https://askubuntu.com/a/229592/771659
- https://docs.wildfly.org/18/Admin_Guide.html
- https://www.keycloak.org/docs/latest/server_installation/#cli-scripting
- https://stackoverflow.com/a/23702185/7748446
