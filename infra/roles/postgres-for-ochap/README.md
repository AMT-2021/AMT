# postgres-for-ochap role

- Install and configure postgresql.
- Create user and database for ochap-backend (sets the db owner to such user).

## Requirements

None.

## Role variables

Variable                | Description                                     | Type    |
--------                | -----------                                     | ----    |
`ochap_backend_db_name` | Name of the database used for ochap-backend     | String  |
`ochap_backend_db_user` | Username of the database user for ochap-backend | String  |
`ochap_backend_db_pass` | Password of the database user for ochap-backend | String  |
