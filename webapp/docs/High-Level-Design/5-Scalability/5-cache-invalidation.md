# Cache Invalidation Strategies

## 🧠 Core Concepts

### Caching Strategy vs Cache Invalidation

- **Caching Strategy** → Defines how data flows between cache and DB
  - Examples: Cache Aside, Write Through, Write Back, Write Around

- **Cache Invalidation** → Defines how cached data is kept **correct/fresh**
  - Examples: TTL, Delete, Update, Event-based, Versioning, SWR

---

## 🔥 Cache Eviction vs Invalidation

- **Eviction** → Removes data due to memory limits (LRU, LFU)
- **Invalidation** → Removes/updates data due to **staleness**

---

# 🚀 Cache Invalidation Strategies

---

## 1. TTL (Time-To-Live)

- Cache expires after a fixed time.

### Pros

- Simple
- No coordination needed

### Cons

- Stale data until expiry
- Hard to tune TTL
- Can cause cache stampede

### Use Cases

- Weather data
- News feeds
- CDN caching

---

## 2. Delete on Write

- Update DB → delete cache
- then on read -> cache miss -> trigger cache addition

### Pros

- Simple
- Widely used

### Cons

- Race conditions
- Multi-key invalidation complexity

### Use Cases

- User profiles
- Product updates

---

## 3. Update on Write

- Update DB → update cache

### Pros

- No stale reads
- Faster than delete + refill

### Cons

- Hard for complex systems
  - In case when for a single DB update there are multiple cache keys need to be updated, we need to fire multiple update request
- Risk of partial inconsistency

### Use Cases

- Small objects
- Counters

---

## 4. Event-Based Invalidation

- Publish event → consumers invalidate cache

### Pros

- Scalable
- Decoupled

### Cons

- Event loss risk
- Debugging complexity

### Use Cases

- Microservices
- Distributed systems

---

## 5. Versioning

- Change key version instead of deleting

### Pros

- Avoids race conditions
- Highly reliable

### Cons

- Memory overhead
- Requires version tracking

### Use Cases

- High-scale systems
- CDN assets

---

## 6. Stale-While-Revalidate (SWR)

- Serve stale data and refresh in background

### Time Windows

- **t1 (Fresh TTL)** → data is fresh
- **t2 (Stale TTL)** → data can be stale but usable

```
0 -------- t1 -------- t2 -------- X
```

### Behavior

- **0 → t1 (Fresh)**
  Return cached data

- **t1 → t2 (Stale)**
  Return cached data + trigger background refresh

- **> t2 (Expired)**
  Fetch fresh data → update cache → return

### Pros

- Fast responses
- Avoids stampede

### Cons

- Temporary stale data
- More complex

### Use Cases

- Feeds
- Product listings

---

# ⚠️ Common Problems

## Cache Stampede

Many requests hit DB simultaneously after expiry

### Solutions

- Jitter
- SWR
- [Request coalescing](../2-Networking/5-request-coalescing.md)
- Locking

---

## Race Conditions

Old data overwriting new cache

### Solutions

- Versioning
- Write-through

---

## Partial Invalidation

One DB update affects multiple cache keys

### Solutions

- Event-driven invalidation
- Tag-based invalidation

---

# Jitter (Important Optimization)

## Idea

Add randomness to TTL

Example:
TTL = 60 ± random(0–10)

## Why

Prevents synchronized expiry → avoids stampede

## Pros

- Smooths load
- Easy to implement

## Cons

- Slight unpredictability

## Best Practice

Combine with:

- TTL
- SWR
- Request coalescing
