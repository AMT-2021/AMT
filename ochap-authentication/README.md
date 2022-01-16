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

//TODO


