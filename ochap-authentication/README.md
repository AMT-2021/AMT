# Authentication server

This **application** provides an auth API to register new users and give JWT token to authenticate user when they logged
into the server


## API description

The API description used for this service can be seen in the [following file](./AuthServerSwagger.json).
This file can be displayed in any swagger editor.

## Requirements

Running the application requires

- A PostgreSQL database.

By default, the application will try to connect to a PostgreSQL cluster on
localhost on port 5432 to database `ochap-authentication` using username
`ochap-authentication-user` and password `ochap-authentication-integration-tests-password`.

## Development server

The development server may be launched with by specifying the
`jwt.secret` and `admin.password` in the command line:

```console
$ mvn spring-boot:run \
  -Dspring-boot.run.jvmArguments="-Djwt.secret=<secret> -Dadmin.password=<secret>"
```

A development version of the API should be accessible at
<http://localhost:8081>.


## Testing

Tests may be run using `mvn test` as expected.

## Production deployment


When run under [Wildfly][], the application expects the configuration module
`ch.heigvd.amt.ochap_authentication.configuration` to be available; moreover the
resource path is expected to contain an `application.properties` file with the
following variables:

Variable                       | Description                               |
--------                       | -----------                               |
`server.port`                  | The port the server runs on               |
`spring.datasource.url`        | Java database connection (JDBC) URL       |
`spring.datasource.username`   | Username to access the database           |
`spring.datasource.password`   | Password to access the database           |
`jwt.secret`                   | Shared secret used to validate JWT tokens |
`admin.password`               | The password for the admin user `ochap`   |


