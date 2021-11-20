# Ô Chap' WARs

Deploys the ochap WARs and configuration files.

## Requirements

- wildfly
- postgresql

## Role variables

Variable                          | Description                                              | Type     |
--------                          | -----------                                              | ----     |
`ochap_backend_db_name`           | Name of the database used for ochap-backend              | String   |
`ochap_backend_db_user`           | Username of the database user for ochap-backend          | String   |
`ochap_backend_db_pass`           | Password of the database user for ochap-backend          | String   |
`ochap_usermgmt_auth_service_url` | Base URL where the authentication service may be reached | String   |
`wars_to_deploy`                  | Path to web application archives to deploy               | [String] |

### Variables with acceptable default values

Variable                | Description                            | Type   |
--------                | -----------                            | ----   |
`ochap_backend_db_host` | Host of the database for ochap-backend | String |
`ochap_backend_db_port` | Port of the database for ochap-backend | String |
