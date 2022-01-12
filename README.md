# Ô Chap'

Ô Chap' is an e-commerce website for selling all kinds of hats!

This application composed of two different modules, each its own web application:

- The e-commerce backend

This application is expected to run under the an application server: The
backend should be mounted under `/`.

The only supported execution environment is within a Wildfly application server,
but other environments may be used for development and debugging.
See the corresponding modules for details.

## Build requirements

- Git
- JDK 11 or later
- Maven 3.2 or later

## Deployment

### Necessary services deployment in a production

- A PostgreSQL database.
- A Wildfly 25.0.1 application server.

### Deployment instructions for production environments

The only supported deployment strategy is using the provided [Ansible][]
playbook under the [infra](infra/) directory.
Deployment is possible to both **the production environment** on Amazon web
services (provided by the teacher) or to a locally-provisioned virtualized
environment.
Please see the corresponding [README](infra/README.md) for details.

[Ansible]: https://www.ansible.com/

### Deployment instructions for local dev/testing

Please see the README of each module
- infrastructure management: [README](infra/README.md)
- backend application : [README](ochap-backend/README.md)

## Contributing

Pull requests or bugfixes are welcome.
For new features or other changes, please open an issue first to discuss what
you would like to change.

Constributions must follow the [workflow](https://github.com/AMT-2021/AMT/wiki/Workflow) described in the wiki.

Please make sure to update tests and documentation accordingly.

## License

[MIT](https://choosealicense.com/licenses/mit/)
