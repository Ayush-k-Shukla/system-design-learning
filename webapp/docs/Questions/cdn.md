# Content Delivery Network

## Requirements

### Functional

1. Serve static content(JS, CSS, images, videos) efficiently
2. Reduce latency for users across diff geo locations
3. provide caching, LB, and failover mechanism
4. Support realtime content invalidation and failover mechanism
5. Write operation for dynamic content
6. Efficient propagation of new Content, with cache invalidation(if file is updated then new cache should update immediately)

### Non-Functional

1. Scalable
2. Low latency
3. Security

## Capacity Estimation

1. Total users - 100M
2. Average content size is 500KB
3. read/write ratio : 100 : 1

### Traffic

| Desc       | value                          |
| ---------- | ------------------------------ |
| QPS(read)  | 100M\*(24*60*60) = 1200 approx |
| QPS(write) | 12                             |

### Storage

| Desc               | value                                     |
| ------------------ | ----------------------------------------- |
| storage per day    | 500KBx1M = 500 GB (excluding replication) |
| storage for 1 year | 181 TB                                    |

### Memory(Cache)

If we follow 80:20 rule for caching

| Desc            | value                           |
| --------------- | ------------------------------- |
| storage per day | 500KBx100M = 50 TB\*0.8 = 40 TB |

## High Level Design

<p align="center">
   <img src="/img/hld-questions/cdn/hld.svg" />
</p>

### Edge Servers (Point of Presence)

1. It is a server which a availabe near to the user
2. It caches the data and reduce load on origin server by fullfilling request at own
3. If content not found then request Origin server

### Origin Servers

1. It is the main server, Original storage for the content
2. If cache miss happens at the Edge server then content served from here
3. Handles content upload

### Global Traffic Manager (Geo-DNS Load balancer)

1. It routes the user request nearest and least loaded edge server
2. Handles the failure by if one edge server down the redirect to other edge server
3. Balance load across all location
4. Based on geographic proximity routes the user to nearest edge server, few approah can be used
   1. GeoDNS - based on IP
   2. Anycast routing - network itself handles nearest node based on BGP(Border gateway protocol)

### Multi-tier Caching

1. Reduce the latency by stroring content at diff cahcing layers like
   1. Client side browser cache
   2. Edge caches(POPs)
   3. Regional Cache - used when pop cache miss but before reaching origin
   4. Origin Cache
2. **Cache Invalidation**
   1. TTL
   2. Event based (on pull remove old)
   3. Versioning - use diff urls for updated content

### Content Replication

1. Ensure every update to origin is propogated across all cdn
2. **Mehods**
   1. Push
   2. Pull
   3. Hybrid - push popular content and pull less popular

### Security

1. Avoid DDos by rate-limiting
2. Token based auth for provate content
3. SSL/TLS encryption for secure delivery

### Read flow

<p align="center">
    <img src="/img/hld-questions/cdn/mermaid-diagram-read-flow.svg"/>
</p>

### Write flow

<p align="center">
    <img src="/img/hld-questions/cdn/mermaid-diagram-write-flow.svg"/>
</p>

## DB Design

### Content metadata (Not clear still)

Stores metadata about CDN-stored content.

```
CREATE TABLE content_metadata (
    id SERIAL PRIMARY KEY,
    url TEXT UNIQUE NOT NULL,  -- CDN URL for the content
    origin_url TEXT NOT NULL,  -- Original source URL
    ttl INT DEFAULT 3600,  -- Time-to-live in seconds
    size BIGINT,  -- Size of content in bytes
    version INT DEFAULT 1,  -- Versioning for updates
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

## API Design

### Get data from cdn

```sh
GET /cdn/content/{content_id}

Response: {"cdn_url": "<cdn_url>,"cache_hit": true}

```

### Upload to cdn

```sh
POST /cdn/upload
body : {"file":binary}
auth token

Response: {"status":OK, "cdn_url": "<cdn_url>,"content_id": string}

```

### Update content (Invalidation)

```sh
PUT /cdn/content/{content_id}
body : {"file":binary,"version":v2}
auth token

Response: {"status":OK, "version": "v2","content_id": string}

```

PENDING

1. DB design
