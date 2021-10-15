# Diagram
### Application architecture
```mermaid
flowchart BT
    Controller -->|/| sf(Static Frontend)
    Controller -->|/hats/id/| fh(Find hat)
    Controller -->|/hats/| ch(Create hat)
    Controller -->|/basic-template/| bt(Basic Template)
    fh --> DB[(Data Base)] 
    ch --> DB
```
