# Authentication server

This **application** provides an auth API to register new users and give JWT token to authenticate user when they logged
into the server

## Requirements

Running the application requires

- A PostgreSQL database.

By default, the application will try to connect to a PostgreSQL cluster on
localhost on port 5432 to database `ochap-authentication` using username
`ochap-authentication-user` and password `ochap-authentication-integration-tests-password`.

Also after the database schema creation after the first time the server is booted, there's the need 
to manually insert the two default roles in the `role` table running the following sql lines:
```sql
INSERT INTO public.role(
	name)
	VALUES ('user');
INSERT INTO public.role(
	name)
	VALUES ('admin');
```

## Development server

The development server may be launched with by specifying the
`jwt.secret` in the command line:

```console
$ mvn spring-boot:run \
  -Dspring-boot.run.jvmArguments="-Djwt.secret=<secret>
```

A development version of the website should be accesible at
<http://localhost:8081>.


## Testing

Tests may be run using `mvn test` as expected.

## Production deployment

//TODO


