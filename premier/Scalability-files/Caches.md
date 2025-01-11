# Caches

1. It is technique of temporarily store copies of data in high speed storage layers to reduce access time.
2. **Benefits**
    1. Performance Improvement
    2. Improved Scalability
    3. Reduced Load
3. **Types**
    1. In-memory
        1. store data in main memory for fast access (Redis, Memcached)
        2. mainly used for session management, store frequently accessed data and as a front for DB.
    2. Distributed 
        1. Cache is shared in multiple nodes (Redis cluster, Amazon Elasticache)
        2. It used for 
            1. Shared session data in microservices.
            2. Global state management for large systems
    3. Client side
        1. Data is cached on client side (browser, mobile app) mainly in the form of cookies, local storage or app specific cache
        2. Used
            1. web browser to cache static assets to increase load time
            2. Offline support for mobile apps
    4. Database
        1. involves storing frequently queried DB result in a cache
        2. Types
            1. Query Cache - cache query result
            2. Index Cache - keep index in-memory
            3. Row Cache - ideal for complex and less changing queries
    5. CDN
        1. a mechanism where static and dynamic content is cached at geographically distributed servers (edge locations) closer to end-users.
        2. It reduces latency, improve page load time
4. **Caching strategies**
    1. **Read through**
        1. Working
            1. The cache act as a transparent proxy between app and data store.
            2. The app always interacts with cache
            3. on cache miss the cache itself fetches the data from the data store, updates the cache, and returns the data to application.
        2. pros
            1. Simple, cache itself handle misses and populates itself
            2. it ensures data is always fetched from single source(cache)
        3. cons
            1. increased complexity in cache logic
            2. cache layer should support read through logic, which may require specific solutions
        4. use case
            1. when a cache system need to be abstracted away from app
    2. **Write through**
        1. Every write to DB is also written to the cache simultaneously
        2. Ensures consistency but impacting write performance
        3. Pros
            1. ensures cache consistency with DB
            2. No stale data
        4. Cons
            1. increased write latency since cache and DB updates are synchronous
        5. use case
            1. systems requiring strong consistency (banking app)
    3. **Write back (Write behind)**
        1. Data is written to cache first and later to synchronized with the database, improving write performance but risking data loss.
        2. use case
            1. write heavy app with tolerance for eventual consistency
    4. **Cache aside (Lazy loading)**
        1. works
            1. The application explicitly interact with cache
            2. if cache miss then application retrieves data from DB updated the cache, and then serves the response
        2. Pros
            1. Simple
            2. Cache only contains requested data (reducing memory usage)
        3. cons
            1. initial cache miss cost lost of overhead 
        4. use case
            1. frequently read with less update (e.g. product catalog)
    5. Write Around
        1. writes goes directly to the database, the cache is updated only when data is read later.
        2. Pros
            1. Reduced cache overhead
        3. cons
            1. Stale data risk
            2. Cold cache - new or updated data is not immediately available in cache
            3. not ideal of read heavy app
        4. use case
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
    1. Cache Coherence
        1. ensures data in cache remains consistent with source of truth(DB)
        2. Solution
            1. Implement TTL to periodically refresh
    2. Cache Invalidation
        1. Determining when and how to update and remove stale data from cache
    3. Cold start
        1. Handling scenarios like when the cache is empty such as during restart.
        2. Solution
            1. Prepopulate data with critical data
            2. use cache aside pattern 
    4. Cache Eviction policy
        1. Choose correct eviction policy
        2. Analyze workload patterns to choose the correct policy
        3. Monitor eviction metrices
    5. Cache penetration
        1. Preventing malicious attempts to repeatedly query for non-existent data, potentially overwhelming the backend.
    6. Cache stampede
        1. When many clients request the same data simultaneously, causing a cache miss and overwhelming the backend data source.
        2. Solution
            1. use locking to allow only one request data while others wait