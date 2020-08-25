# Keycloak Mailchimp

1. Add this inside the Keycloak `standalone*.xml` you're using:
    ```xml
    <spi name="eventsListener">
        <provider name="mailchimp" enabled="true">
            <properties>
                <property name="API_KEY" value="api-key"/>
                <property name="LIST_ID" value="api-value"/>
            </properties>
        </provider>
    </spi>
    ```

2. Now, add `target/*.jar` files into `providers` folder of your keycloak.

3. Then start.
