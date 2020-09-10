# Keycloak Mailchimp


## Manual config
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

## Auto config
1. Run this script `startup.cli`. When doing this, replace `SOME_API_KEY` and `SOME_LIST_ID`
by your own data.

2. Now, add `target/*.jar` files into `providers` folder of your keycloak.

3. Then start.
