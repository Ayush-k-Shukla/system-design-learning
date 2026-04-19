# Core Failure Patterns (Part 2)

- This document summarizes some key production failure patterns, their root causes, impacts, solutions, and—most importantly—how they connect in real-world systems.

---

### 1. N+1 Query Problem

**Root Cause:** Fetching related data with multiple queries instead of batching.

**Impact:**

- Database load explodes: O(N) queries instead of O(1)
- Latency increases linearly
- Under load → DB becomes bottleneck → cascading failures

**Solutions:**

- JOIN queries
  ```
  SELECT o.*, u.* FROM orders o JOIN users u ON o.user_id = u.id;
  ```
- Batch fetching (IN queries)
  ```
  SELECT * FROM users WHERE id IN (...)
  ```
- DataLoader pattern
  - It helps by batching and caching requests within a single execution cycle.
  - collect all requests → batch them → fetch in one go
  - Used heavily in a GraphQL system
- Denormalization

---

## 2. Single Point of Failure (SPOF)

**Root Cause:** Critical component exists only once.

**Impact:**

- Full system downtime
- No fault tolerance

**Solutions:**

- Replication
- Load balancing
- Multi-region deployment
- Leader election (for distributed systems)
- Failover mechanisms and health monitor

---

## 3. Backpressure

**Root Cause:** Producer send messaged more than the speed consumer can consume.

**Impact:**

- Queue buildup
- Memory issues
- Crashes

**Solutions:**

- Rate limiting
- Bounded queues
- Autoscaling
- Load shedding
  - drop excess data

---

## 4. Duplicate Requests / Idempotency Gap

**Root Cause:** Same request processed multiple times.

**Impact:**

- Duplicate transactions
- Data inconsistency

**Solutions:**

- Idempotency keys
- Deduplication
  - Store in deduplicated DB like (redis)
- Safe API design
  - Use PUT instead of POST where possible

---

## 5. Stale Cache / Read-After-Write Inconsistency

**Root Cause:** Cache not updated after write.

**Impact:**

- Outdated data
- Inconsistent reads

**Solutions:**

- Cache invalidation
  - Delete cache on write
  - Next read fetches fresh data
- Write-through cache
  - Update cache + DB together
- SWR strategy
  - Serve stale data + refresh in background
- Versioning / timestamps
  - serve latest version/timestamp data

---

## Failure Chain (Real Production Scenario)

- Not always all occurs

```
Hot Key →
    Single shard overload →
        Latency increase →
            Timeouts →
                Retry Storm →
                    Thundering Herd →
                        Cache Stampede →
                            DB Overload →
                                Backpressure →
                                    Dropped Requests →
                                        Duplicate Retries →
                                            Inconsistent State
```

---

## Core Categories

### 1. Load Imbalance

- Hot key
- N+1

### 2. Load Amplification

- Retry storm
- Thundering herd
- Cache stampede

### 3. Load Mismanagement

- Backpressure
- Stale cache
- Idempotency gap
- SPOF
