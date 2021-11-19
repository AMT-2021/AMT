# User account creation and authentication

This **application** consumes an external service for user management,
henceforth _the authentication service_.
This application serves the pages for a user to create an account or login.

## Deployment

The application is expected to run on a [Wildfly][] application server.

[Wildfly]: https://www.wildfly.org/

When run under [Wildfly][], the application expects the configuration module
`ch.heigvd.amt.ochap_usermgmt.configuration` to be available; moreover the
resource path is expected to contain an `application.properties` file with the
following variables:

Variable              | Description                             |
--------              | -----------                             |
`authServiceBaseUrl`  | Base URL of the authentication service  |

## Testing

Tests may be run using `mvn test` as expected.
Mind that some tests require real credentials to the external authentication
service and are thus skipped by default.
If this credentials are available, these test can be run using the following
command:

```console
$ mvn test -Dtest='AmtAuthServiceTests' \
  -DauthServiceBaseUrl=http://localhost:22345
```
