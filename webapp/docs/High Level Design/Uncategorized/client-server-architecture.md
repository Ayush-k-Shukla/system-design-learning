# Client Server Architecture

1. In this multiple clients (users or devices) interact with a centralized server to access data, resource, or services.

## Types

### Two-Tier

1. Here client directly interact with server.
2. The server typically handles both application logic and data management.
3. Simple but ineffecient as number of client increases
4. e.g. a desktop app that directly connects to DB server to retrieve and show data.(MongoDB compass)

### Three-Tier

1. Here Database are handleded on differe server
2. client -> App Logic server -> DB server
3. e.g. a web app which follow it.

### N-Tier

1. extension of 3 tier model, where additional layer can be caching, load balancing, security.

## Advantages

1. Centralized Management
2. Scalability
3. Resource sharing
4. Security

## Challenges and Considerations

1. SPOF - if server down all client will be affected
2. Complex to manage
