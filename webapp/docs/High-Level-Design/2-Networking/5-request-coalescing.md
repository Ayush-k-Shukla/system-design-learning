# Request Coalescing

- Request coalescing is a technique where **multiple identical concurrent requests are merged into a single execution**, and the **same response is shared** with all waiting callers.

---

## Why

Without coalescing:

- 1000 identical requests → 1000 DB/API calls

With coalescing:

- 1000 identical requests → **1 DB/API call** → shared response

---

## Types

## 1. In-Memory Request Coalescing (Single Instance)

- Works only

### Working

- Maintain a **global in-flight map** in server memory
- where Key → Promise / Future

**Flow:**

1. Request arrives
2. If key exists → return same Promise
3. If not → create Promise, store it, execute work
4. On completion → resolve Promise, remove from map

### Key Points and Limitations

- In-flight store is **created once per server process**
- All API calls handled by that instance share it
- Works only **per instance**
- Not effective in multi-server deployments

---

## 2. Redis-Based Request Coalescing (Multi-Instance)

- Used when multiple API servers run behind a load balancer.

### Redis Keys

- Cache key: `resource:{id}`
- Lock key: `lock:resource:{id}`

## Redis Coalescing Flow

### Step 1: Check Cache

- If cache hit → return response
- If cache miss → continue

### Step 2: Acquire Distributed Lock

```
SET lock:resource:{id} NX PX <ttl>
```

- NX → only one server can acquire
- PX → TTL to avoid deadlocks

### Step 3A: Lock Acquired (Leader)

- Fetch data from DB / service
- Store result in Redis cache
- Release lock
- Return response

### Step 3B: Lock Not Acquired (Followers)

- Do NOT hit DB
- Wait until cache is populated

  - Poll cache OR
  - Subscribe via Pub/Sub

### Step 4: Response Fan-out

- All waiting requests read from cache
- DB call executed only once

## Visual Overview

```
Requests ──> Redis Lock ──> DB (once) ──> Cache ──> All callers
```

## Failure Handling (Important)

- **Leader crashes** → lock expires via TTL
- **Slow DB call** → extend lock TTL if needed
- **Retry storm** → add backoff or jitter

---

## In-Memory vs Redis Coalescing

| Aspect          | In-Memory     | Redis-Based     |
| --------------- | ------------- | --------------- |
| Scope           | Single server | All servers     |
| Latency         | Very low      | Slightly higher |
| Complexity      | Simple        | Medium          |
| Fault tolerance | Low           | High            |

---

## When to Use / Benefits

- Expensive DB or API calls
- High read concurrency
- Cache stampede risk
- Rate-limited downstream services
- Reduced DB / downstream load
- Lower latency under burst traffic
- Prevents cache stampede
