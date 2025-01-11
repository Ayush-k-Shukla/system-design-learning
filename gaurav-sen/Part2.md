# Part2

1. Cache
   1. Cache use case
      1. avoid network calls (storing a profile of each user in an in-memory cache)
      2. avoid repeated computation (find average ages of users) - we can precompute and store
      3. Reduce DB calls
   2. when we want to load and evict our data from the cache is called `cache-policy`. LRU(Least Recently Used) is the most common policy.
   3. Cache invalidation: [LINK](https://www.geeksforgeeks.org/cache-invalidation-and-the-methods-to-invalidate-cache/#why-cache-invalidation-is-important)
   4. Where to add cache
      1. near to the server (in memory)
         1. fast and simple to Implement
         2. increases server memory size
      2. Global cache
         1. Like Redis so safe from server failures
         2. higher accuracy
         3. can scale independently
   5. Distributed cache
2. API design(things to consider)
   1. Naming (based on what it does)
   2. Define parameters (avoid passing parameters if not necessary)
   3. Define Response (prefer objects as they are extensible)
   4. Define Errors
   5. Use GET/POST
   6. Side effect (avoid API having side effects)
      1. a side-effect is like you defined an API **setAdmin(groupId, admins);** if groupId is not present, you are creating a group that will be a side-effect.
   7. Pagination and Fragmentation (when API response is huge)
3. Event-driven services
   1. Pub/Sub
