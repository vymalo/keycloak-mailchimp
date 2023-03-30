# Keycloak Mailchimp

## ⚠️ Depreciation notice ⚠️
![img.png](img.png)

I'm not working on this plugin anymore, because I think, given the direction Keycloak is taking, it would be better to have this functionality as an external service, outside Keycloak.

I suggest using a plugin like [this](https://github.com/jessylenne/keycloak-event-listener-http) or [this other](https://github.com/softwarefactory-project/keycloak-event-listener-mqtt) to send events to that other service, which would handle things better.

## Usage

After installing the plugin, it should normally create an endpoint __UNPROTECTED__ at `<keycloak-server-url>:<port>/realms/<realm>/mailchimp-resource/config`. You can call it to get the current mailchimp configuration.

Because it is not protected, you can POST EndPoint to create a new configuration for mailchimp to send a request. Then configure the plugin on the administration page. Example:

```json
{
    "apiKey": "mailchimp-api-key",
    "listId": "mailchimp-list-id",
    "listenedEvents": [
      "LOGIN", "REGISTER"
    ]
}
```
As we are working with Keycloak, keep in mind that the events handled by Keycloak are enums imported from `org.keycloak.events.EventType`.

You will then have a configuration for a specific domain. That's it. The rest is under the hood.

## Under the hood

1. Event Listener Provider
2. JPA entity provider
3. Resource provider

These providers are used to keep the plugin configuration running.

## Links

- https://github.com/p2-inc/keycloak-events
