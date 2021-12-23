# User account creation and authentication

This **application** consumes an external service for user management,
henceforth _the authentication service_.
This application serves the pages for a user to create an account or login.

## Requirements

Running the application requires

- The URL to the external user authentication service.

## Testing

Tests may be run using `mvn test` as expected.
Mind that some tests require contacting the external authentication service
and are thus skipped by default.
If a connection to the external authentication service is available, these test
can be run using the following command:

```console
$ mvn test -Dtest='AmtAuthServiceTests' \
  -DauthServiceBaseUrl=http://localhost:22345
```

## Production deployment

> Using the provided automation in the [infra](../infra) directory is strongly
> recommended, that directory automates all of the procedure described in this
> section.

The application is expected to run on a [Wildfly][] application server mounted
under `/users`.

[Wildfly]: https://www.wildfly.org/

When run under [Wildfly][], the application expects the configuration module
`ch.heigvd.amt.ochap_usermgmt.configuration` to be available; moreover the
resource path is expected to contain an `application.properties` file with the
following variables:

Variable              | Description                             |
--------              | -----------                             |
`authServiceBaseUrl`  | Base URL of the authentication service  |
