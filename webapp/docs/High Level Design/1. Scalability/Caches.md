# 🗄️ Caches

> **A cache is a high-speed data storage layer that stores a subset of data, typically transient in nature, so that future requests for that data are served up faster than by accessing the underlying slower storage layer.**

---

## 🚀 Benefits

- **Performance Improvement**: Faster data access.
- **Improved Scalability**: Reduces backend load.
- **Reduced Latency**: Quicker response times for users.

---

## 📦 Types of Caches

### In-memory Cache

- Stores data in main memory for ultra-fast access (e.g., **Redis**, **Memcached**)
- Used for session management, frequently accessed data, and as a front for databases.

### Distributed Cache

- Cache is shared across multiple nodes (e.g., **Redis Cluster**, **Amazon ElastiCache**)
- Use cases:
  - Shared session data in microservices
  - Global state management for large systems

### Client-side Cache

- Data is cached on the client (browser, mobile app) using cookies, local storage, or app-specific cache.
- Use cases:
  - Web browsers cache static assets to decrease load time
  - Offline support for mobile apps

### Database Cache

- Frequently queried DB results are cached.
- Types:
  - **Query Cache**: Caches query results
  - **Index Cache**: Keeps indexes in memory
  - **Row Cache**: Ideal for complex, less-changing queries

### CDN (Content Delivery Network)

- Static and dynamic content is cached at geographically distributed edge servers closer to end-users.
- Reduces latency and improves page load time.

---

## 🔄 Caching Strategies

### Read Through

**How it works:**

> The cache acts as a transparent proxy between the app and the data store. The app always interacts with the cache. On a cache miss, the cache fetches data from the data store, updates itself, and returns the data.

**Pros:**

- Simple; cache handles misses and populates itself.
- Ensures data is always fetched from a single source (the cache).

**Cons:**

- Increased complexity in cache logic.
- Cache layer must support read-through logic.

**Use case:**

- When the cache system needs to be abstracted away from the app

---

### Write Through

**How it works:**

> Every write to the DB is also written to the cache simultaneously. Ensures consistency but may impact write performance.

**Pros:**

- Ensures cache consistency with DB
- No stale data

**Cons:**

- Increased write latency (cache and DB updates are synchronous)

**Use case:**

- Systems requiring strong consistency (e.g., banking apps)

---

### Write Back (Write Behind)

**How it works:**

> Data is written to the cache first and later synchronized with the database. Improves write performance but risks data loss.

**Use case:**

- Write-heavy apps with tolerance for eventual consistency

---

### Cache Aside (Lazy Loading)

**How it works:**

> The application explicitly interacts with the cache. On a cache miss, the app retrieves data from the DB, updates the cache, and serves the response.

**Pros:**

- Simple
- Cache only contains requested data (reducing memory usage)

**Cons:**

- Initial cache miss incurs overhead

**Use case:**

- Frequently read, less updated data (e.g., product catalog)

---

### Write Around

**How it works:**

> Writes go directly to the database; the cache is updated only when data is read later.

**Pros:**

- Reduced cache overhead

**Cons:**

- Stale data risk
- Cold cache (new/updated data not immediately available)
- Not ideal for read-heavy apps

**Use case:**

- Write-heavy systems

---

![Caching Strategies](https://substackcdn.com/image/fetch/f_auto,q_auto:good,fl_progressive:steep/https%3A%2F%2Fbucketeer-e05bbc84-baa3-437e-9518-adb32be77984.s3.amazonaws.com%2Fpublic%2Fimages%2F3d4f861a-82b2-4b8f-b9c7-d926f079a108_2163x3153.jpeg)

---

## 🧹 Cache Eviction Policies

To manage limited cache size, eviction policies determine which data to remove when the cache is full.

### LRU (Least Recently Used)

- Removes the least recently accessed item first
- Assumes recently accessed data will likely be accessed again soon
- Often implemented using a doubly linked list
- Best for data with temporal locality

### LFU (Least Frequently Used)

- Removes the least frequently accessed items first
- Assumes frequently accessed data is more valuable
- Maintains a count of access frequencies for each item
- Ideal for scenarios with repeated patterns (e.g., recommendation systems)

### FIFO (First In, First Out)

- Removes the oldest data first, regardless of access frequency
- Simple and deterministic
- Implemented using a queue
- Used when data freshness is more important

### TTL (Time To Live)

- Time-based eviction; data is removed after a specified duration
- Ensures data freshness and consistency
- Suitable for time-sensitive data (e.g., stock prices, weather forecasts)

---

## ⚠️ Challenges & Considerations

### Cache Coherence

- Ensures cache data remains consistent with the source of truth (DB)
- **Solution:** Implement TTL to periodically refresh

### Cache Invalidation

- Determining when and how to update/remove stale data from cache

### Cold Start

- Handling scenarios when the cache is empty (e.g., after restart)
- **Solution:** Prepopulate with critical data or use cache-aside pattern

### Cache Eviction Policy

- Choose the correct eviction policy based on workload patterns
- Monitor eviction metrics

### Cache Penetration

- Prevent malicious attempts to repeatedly query non-existent data, overwhelming the backend

### Cache Stampede

- When many clients request the same data simultaneously, causing a cache miss and overwhelming the backend
- **Solution:** Use locking to allow only one request to fetch data while others wait
