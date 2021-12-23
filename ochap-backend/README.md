# Backend application

This **application** provides the user with access to the catalog and allows
them to manage their shopping cart and ultimately order (not yet implemented).
It is the main entry point of the application.

## Requirements

Running the application requires

- A PostgreSQL database.
- The JWT secret shared with the authentication server.
- A directory for storing data.

By default, the application will try to connect to a PostgreSQL cluster on
localhost on port 5432 to database `ochap-backend` using username
`ochap-backend-user` and password `ochap-backend-integration-tests-password`.
The application also uses a default directory for its data, but a valid JWT
secret is still required.

## Development server

The development server may be launched with by specifying the
`authServiceJwtSecret` in the command line:

```console
$ mvn spring-boot:run \
  -Dspring-boot.run.jvmArguments="-DauthServiceJwtSecret=<secret>"
```

A development version of the website should be accesible at
<http://localhost:8080>.

Mind that testing the login and register functionnalities is not possible in a
development setup since they reside in the user-management web application
(which is a separate module).

> If you want to test behaviour requiring an authenticated user, you have
> to generate a valid JWT token (See the user-management web application README)
> and then insert a cookie named `token` with path `/` and value corresponding
> to the JWT token in your web browser.

> On firefox, this can be done by right-clicking on the website canvas > Inspect
> > Storage tab > Cookies > http://localhost > Plus symbol (add item).

> Be advised that if you use an invalid token (e.g. expired), the website will
> display a 500 error. Try removing and readding the token in this case.
> This is a known bug.

## Testing

Tests may be run using `mvn test` as expected.
Mind that some tests require real credentials to the external authentication
service and are thus skipped by default.

```console
$ mvn test -Dtest='AmtAuthServiceTests' -DauthServiceBaseUrl=http://localhost:49123
```

## Production deployment

> Using the provided automation in the [infra](../infra) directory is strongly
> recommended, that directory automates all of the procedure described in this
> section.

The application is expected to run on a [Wildfly][] application server mounted
under `/`.

[Wildfly]: https://www.wildfly.org/

When run under [Wildfly][], the application expects the configuration module
`ch.heigvd.amt.ochap_backend.configuration` to be available; moreover the
resource path is expected to contain an `application.properties` file with the
following variables:

Variable                       | Description                               |
--------                       | -----------                               |
`spring.datasource.url`        | Java database connection (JDBC) URL       |
`spring.datasource.username`   | Username to access the database           |
`spring.datasource.password`   | Password to access the database           |
`authServiceJwtSecret`         | Shared secret used to validate JWT tokens |
`ch.heivd.amt.backend.datadir` | Directory where the app can store data    |

> The syntax of the connection URL depends on the database driver.
> We only support postgres.
> An example postgres JDBC connection URL looks like this:
> `jdbc:postgresql://localhost:5432/ochap-backend`.

This application requires the `ochap-user-management` application to be mounted
under `/users/` for login functionnality.
