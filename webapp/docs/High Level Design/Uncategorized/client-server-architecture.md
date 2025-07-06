# 🖥️ Client-Server Architecture

1. In this model, multiple clients (users or devices) interact with a centralized server to access data, resources, or services.

---

## Types

### Two-Tier

1. Here, the client directly interacts with the server.
2. The server typically handles both application logic and data management.
3. Simple but inefficient as the number of clients increases.
4. Example: A desktop app that directly connects to a DB server to retrieve and show data (e.g., MongoDB Compass).

### Three-Tier

1. Here, databases are handled on a different server.
2. Client → App Logic Server → DB Server
3. Example: A web app which follows this model.

### N-Tier

1. Extension of the 3-tier model, where additional layers can be caching, load balancing, security, etc.

---

## Advantages

1. Centralized management
2. Scalability
3. Resource sharing
4. Security

---

## Challenges and Considerations

1. SPOF (Single Point of Failure) – If the server is down, all clients will be affected.
2. Complex to manage
