# Web application components

```mermaid
graph LR
  subgraph usermgmt
    Controller
    AuthService
    AuthClient["AuthServiceWebClient"]

    Controller --> AuthService --> AuthClient
  end

  AuthClient --> ExternalAuthService["External auth service"]
  Clients -->|/users/*| Controller
```
