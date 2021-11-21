# Backend application

This **application** provides the user with access to the catalog and allows
them to manage their shopping cart and ultimately order (not yet implemented).
It is the main entry point of the application.

## Deployment

The application is expected to run on a [Wildfly][] application server.

[Wildfly]: https://www.wildfly.org/

When run under [Wildfly][], the application expects the configuration module
`ch.heigvd.amt.ochap_backend.configuration` to be available; moreover the
resource path is expected to contain an `application.properties` file with the
following variables:

Variable                     | Description                               |
--------                     | -----------                               |
`spring.datasource.url`      | Java database connection (JDBC) URL       |
`spring.datasource.username` | Username to access the database           |
`spring.datasource.password` | Password to access the database           |
`authServiceJwtSecret`       | Shared secret used to validate JWT tokens |

> The syntax of the connection URL depends on the database driver.
> We only support postgres.
> An example postgres JDBC connection URL looks like this:
> `jdbc:postgresql://localhost:5432/ochap-backend`.

This application requires the `ochap-user-management` application to be mounted
under `/users/` for login functionnality.

## Testing

Tests may be run using `mvn test` as expected.
Mind that some tests require real credentials to the external authentication
service and are thus skipped by default.

## Development server

The development server may be launched with by specifying the
`authServiceJwtSecret` in the command line:

```console
$ mvn spring-boot:run \
  -Dspring-boot.run.jvmArguments="-DauthServiceJwtSecret=<secret>"
```
