# 🖥️ Client-Server Architecture

- In this model, multiple clients (users or devices) interact with a centralized server to access data, resources, or services.

## Types

### Two-Tier

- Here, the client directly interacts with the server.
- The server typically handles both application logic and data management.
- Simple but inefficient as the number of clients increases.
- Example: A desktop app that directly connects to a DB server to retrieve and show data (e.g., MongoDB Compass).

### Three-Tier

- Here, databases are handled on a different server.
- Client → App Logic Server → DB Server
- Example: A web app which follows this model.

### N-Tier

- Extension of the 3-tier model, where additional layers can be caching, load balancing, security, etc.

## Advantages

- Centralized management
- Scalability
- Resource sharing
- Security

## Challenges and Considerations

- SPOF (Single Point of Failure) – If the server is down, all clients will be affected.
- Complex to manage
