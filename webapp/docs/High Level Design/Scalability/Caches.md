# 🗄️ Caches

> **A cache is a high-speed data storage layer that stores a subset of data, typically transient in nature, so that future requests for that data are served up faster than by accessing the underlying slower storage layer.**

---

## 🚀 Benefits

- **Performance Improvement**: Faster data access.
- **Improved Scalability**: Reduces backend load.
- **Reduced Latency**: Quicker response times for users.

---

## 📦 Types of Caches

### 1. In-memory Cache

- Stores data in main memory for ultra-fast access (e.g., **Redis**, **Memcached**)
- Used for session management, frequently accessed data, and as a front for databases.

### 2. Distributed Cache

- Cache is shared across multiple nodes (e.g., **Redis Cluster**, **Amazon ElastiCache**)
- Use cases:
  - Shared session data in microservices
  - Global state management for large systems

### 3. Client-side Cache

- Data is cached on the client (browser, mobile app) using cookies, local storage, or app-specific cache.
- Use cases:
  - Web browsers cache static assets to decrease load time
  - Offline support for mobile apps

### 4. Database Cache

- Frequently queried DB results are cached.
- Types:
  - **Query Cache**: Caches query results
  - **Index Cache**: Keeps indexes in memory
  - **Row Cache**: Ideal for complex, less-changing queries

### 5. CDN (Content Delivery Network)

- Static and dynamic content is cached at geographically distributed edge servers closer to end-users.
- Reduces latency and improves page load time.

---

## 🔄 Caching Strategies

### 1. Read Through

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

### 2. Write Through

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

### 3. Write Back (Write Behind)

**How it works:**

> Data is written to the cache first and later synchronized with the database. Improves write performance but risks data loss.

**Use case:**

- Write-heavy apps with tolerance for eventual consistency

---

### 4. Cache Aside (Lazy Loading)

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

### 5. Write Around

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

### 1. LRU (Least Recently Used)

- Removes the least recently accessed item first
- Assumes recently accessed data will likely be accessed again soon
- Often implemented using a doubly linked list
- Best for data with temporal locality

### 2. LFU (Least Frequently Used)

- Removes the least frequently accessed items first
- Assumes frequently accessed data is more valuable
- Maintains a count of access frequencies for each item
- Ideal for scenarios with repeated patterns (e.g., recommendation systems)

### 3. FIFO (First In, First Out)

- Removes the oldest data first, regardless of access frequency
- Simple and deterministic
- Implemented using a queue
- Used when data freshness is more important

### 4. TTL (Time To Live)

- Time-based eviction; data is removed after a specified duration
- Ensures data freshness and consistency
- Suitable for time-sensitive data (e.g., stock prices, weather forecasts)

---

## ⚠️ Challenges & Considerations

### 1. Cache Coherence

- Ensures cache data remains consistent with the source of truth (DB)
- **Solution:** Implement TTL to periodically refresh

### 2. Cache Invalidation

- Determining when and how to update/remove stale data from cache

### 3. Cold Start

- Handling scenarios when the cache is empty (e.g., after restart)
- **Solution:** Prepopulate with critical data or use cache-aside pattern

### 4. Cache Eviction Policy

- Choose the correct eviction policy based on workload patterns
- Monitor eviction metrics

### 5. Cache Penetration

- Prevent malicious attempts to repeatedly query non-existent data, overwhelming the backend

### 6. Cache Stampede

- When many clients request the same data simultaneously, causing a cache miss and overwhelming the backend
- **Solution:** Use locking to allow only one request to fetch data while others wait

4. **Caching strategies**

   1. **Read through**

      1. **Working**
         1. The cache act as a transparent proxy between app and data store.
         2. The app always interacts with cache
         3. on cache miss the cache itself fetches the data from the data store, updates the cache, and returns the data to application.
      2. **pros**
         1. Simple, cache itself handle misses and populates itself
         2. it ensures data is always fetched from single source(cache)
      3. **cons**
         1. increased complexity in cache logic
         2. cache layer should support read through logic, which may require specific solutions
      4. **use case**
         1. when a cache system need to be abstracted away from app

   2. **Write through**

      1. Every write to DB is also written to the cache simultaneously
      2. Ensures consistency but impacting write performance
      3. **Pros**
         1. ensures cache consistency with DB
         2. No stale data
      4. **Cons**
         1. increased write latency since cache and DB updates are synchronous
      5. **use case**
         1. systems requiring strong consistency (banking app)

   3. **Write back (Write behind)**

      1. Data is written to cache first and later to synchronized with the database, improving write performance but risking data loss.
      2. **use case**
         1. write heavy app with tolerance for eventual consistency

   4. **Cache aside (Lazy loading)**

      1. **works**
         1. The application explicitly interact with cache
         2. if cache miss then application retrieves data from DB updated the cache, and then serves the response
      2. **Pros**
         1. Simple
         2. Cache only contains requested data (reducing memory usage)
      3. **cons**
         1. initial cache miss cost lost of overhead
      4. **use case**
         1. frequently read with less update (e.g. product catalog)

   5. **Write Around**

      1. writes goes directly to the database, the cache is updated only when data is read later.
      2. **Pros**
         1. Reduced cache overhead
      3. **cons**
         1. Stale data risk
         2. Cold cache - new or updated data is not immediately available in cache
         3. not ideal of read heavy app
      4. **use case**
         1. Write heavy systems

      ![https://substackcdn.com/image/fetch/f_auto,q_auto:good,fl_progressive:steep/https%3A%2F%2Fbucketeer-e05bbc84-baa3-437e-9518-adb32be77984.s3.amazonaws.com%2Fpublic%2Fimages%2F3d4f861a-82b2-4b8f-b9c7-d926f079a108_2163x3153.jpeg](https://substackcdn.com/image/fetch/f_auto,q_auto:good,fl_progressive:steep/https%3A%2F%2Fbucketeer-e05bbc84-baa3-437e-9518-adb32be77984.s3.amazonaws.com%2Fpublic%2Fimages%2F3d4f861a-82b2-4b8f-b9c7-d926f079a108_2163x3153.jpeg)

5. **Cache Eviction Policies**

   1. To Manage limited size of cache, eviction policies are used to determine which data should be remove when cache is full
   2. **LRU (Least recently)**
      1. Removes the least recently accessed item first
      2. Assumes that recently accessed data will likely to be accessed again soon
      3. Often implemented using Doubly linked list
      4. Used mostly when data access patterns exhibit temporal locality (The core is likely to access the *same* location again in the near future.)
   3. **LFU (Least Frequently)**
      1. Removes the least frequently accessed items first
      2. Assume that frequently accessed data is more valuable and should remain in cache
      3. Implemented by maintaining a count of access frequencies for each item.
      4. Ideal for the scenarios with clear, repeated patterns (e.g. recommendation systems)
   4. **FIFO**
      1. removes the oldest data first regardless of how often or recently data is accessed.
      2. Simple and deterministic
      3. Implemented using queue
      4. Used when freshness of data is more important
   5. **TTL**
      1. It is a time based eviction policy where data is removed from the cache after a specified duration
      2. Ensures data freshness and consistency
      3. Suitable for caches that deal with time sensitive data (e.g. stock prices, weather forecasts)

6. **Challenges and considerations**

   1. **Cache Coherence**
      1. ensures data in cache remains consistent with source of truth(DB)
      2. Solution
         1. Implement TTL to periodically refresh
   2. **Cache Invalidation**
      1. Determining when and how to update and remove stale data from cache
   3. **Cold start**
      1. Handling scenarios like when the cache is empty such as during restart.
      2. Solution
         1. Prepopulate data with critical data
         2. use cache aside pattern
   4. **Cache Eviction policy**
      1. Choose correct eviction policy
      2. Analyze workload patterns to choose the correct policy
      3. Monitor eviction metrices
   5. **Cache penetration**
      1. Preventing malicious attempts to repeatedly query for non-existent data, potentially overwhelming the backend.
   6. **Cache stampede**
      1. When many clients request the same data simultaneously, causing a cache miss and overwhelming the backend data source.
      2. Solution
         1. use locking to allow only one request data while others wait
