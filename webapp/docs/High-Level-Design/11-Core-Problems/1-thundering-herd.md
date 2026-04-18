# Core Failure Patterns (Part 1)

Some of them are

- Thundering Herd Problem
- Cache Stampede
- Retry Storm

---

# Thundering Herd Problem

It occurs when a large number of clients, threads, or processes wake up or act simultaneously and compete for the same resource, overwhelming the system.

## How It Happens

- Multiple clients wait on the same event (cache expiry, lock release, service recovery)
- Event occurs
- All clients act at once

### Example Flow

1. Cache expires at time T
2. Thousands of requests arrive at time T
3. All requests hit backend simultaneously
4. Backend gets overloaded

## Key Characteristics

- Synchronized behavior
- Sudden spike in traffic (not gradual)
- Resource contention (CPU, DB, locks)

## Real-World Scenarios

- Cache keys expiring at the same time
- Threads waiting on a mutex
- Services recovering after downtime

## Impact

- Latency spikes
- Database overload
- Cascading failures
- Reduced system availability

## Solutions & Approaches

### TTL Jitter (Highly Effective)

Instead of fixed expiration:

```
TTL = base + random_offset
```

Prevents synchronized expiration.

### Rate Limiting

Limit how many requests reach backend.

### Load Shedding

Drop excess traffic to protect system.

### Queueing

Buffer requests instead of processing all at once.

### Backoff Mechanisms

Delay retries to avoid synchronization.

---

# Cache Stampede

Cache stampede occurs when multiple requests encounter a cache miss simultaneously and all attempt to regenerate the same data, overloading the backend.

## How It Happens

1. Cached data expires
2. Many requests arrive
3. All requests miss cache
4. All requests fetch from DB and try to populate cache

## Key Characteristics

- Duplicate work
- Backend overload
- Happens specifically on cache miss

## Difference from Thundering Herd

- Thundering herd = broader concept
- Cache stampede = specific case involving cache recomputation

## Real-World Scenario

- Popular API response cached
- Cache expires
- Thousands of requests hit DB simultaneously

## Impact

- Redundant DB queries
- Increased latency
- Possible system crash

## Solutions & Approaches

### Distributed Locking (Critical)

Ensure only one request recomputes data.

Approach:

- Acquire lock
- One request fetches data
- Others wait or retry

### Request Coalescing (Single Flight)

[More Details for Request Coalescing](../2-Networking/5-request-coalescing.md)

- First request triggers fetch
- Others reuse same promise/result

### Stale-While-Revalidate (SWR)

[More Details for SWR](../5-Scalability/5-cache-invalidation.md#6-stale-while-revalidate-swr)

- Serve stale data immediately
- Refresh in background

Benefits:

- Low latency
- No spike in backend load

### Early Refresh (Refresh Ahead)

- Refresh cache before expiry
- Done via background jobs

### TTL Jitter

Avoid simultaneous expiration.

## Production Strategy (Best Practice)

Combine:

- SWR + Locking + Jitter

## When to Use

- Frequently accessed data
- Expensive DB queries
- High concurrency systems

---

# Retry Storm

A retry storm occurs when many clients retry failed requests aggressively and simultaneously, amplifying load and preventing system recovery.

## How It Happens

1. Service starts failing (timeout/error)
2. Clients retry immediately
3. Retries increase load
4. System degrades further
5. More retries are triggered

## Key Characteristics

- Feedback loop
- Traffic amplification
- Self-inflicted overload

## Real-World Scenario

- Database latency increases
- API timeouts occur
- Clients retry instantly
- Traffic multiplies (e.g., 10k → 50k requests)

## Impact

- Prevents system recovery
- Cascading failures
- Increased downtime

## Solutions & Approaches

### Exponential Backoff (Essential)

```
delay = base * 2^attempt
```

Reduces retry rate over time.

### Jitter (Critical)

```
delay = base * 2^attempt + random
```

Prevents retry synchronization.

### Retry Limits

- Cap number of retries
- Avoid infinite loops

### Circuit Breaker

- Stop requests when failure threshold reached
- Allow recovery window

States:

- Closed (normal)
- Open (fail fast)
- Half-open (test recovery)

### Rate Limiting

Control incoming traffic.

### Idempotency

Ensure retries do not cause duplicate side effects.

### Timeouts

Fail fast instead of holding resources.

## Production Strategy (Best Practice)

Combine:

- Backoff + Jitter + Circuit Breaker + Retry Limits

## When to Use

- External service calls
- Distributed systems
- Microservices architecture

---

# Final Comparison

| Problem         | Core Issue            | Trigger               | Main Risk         |
| --------------- | --------------------- | --------------------- | ----------------- |
| Thundering Herd | Simultaneous requests | Event synchronization | Resource overload |
| Cache Stampede  | Concurrent cache miss | Cache expiry          | DB overload       |
| Retry Storm     | Excess retries        | Failures              | Traffic explosion |

---

# Key Takeaways

- These problems often occur together during outages
- Synchronization is the root cause in most cases
- Randomization (jitter) is a powerful mitigation tool
- Combining multiple strategies is necessary in production systems
