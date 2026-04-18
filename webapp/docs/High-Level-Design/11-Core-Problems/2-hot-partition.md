# Hot Key / Hot Partition Problem

A **hot key** is a key that receives disproportionately high traffic compared to others. Because distributed systems shard data using a key, a hot key causes:

- One shard → overloaded
- Other shards → underutilized

This leads to a **hot partition**

---

# Why This Happens

- In Typical sharding we do

```
hash(key) → shard

eg:
product:123 → shard 3
```

- If `product:123` becomes viral:
  - All requests go to shard 3
  - System becomes bottlenecked

---

# Why It’s Dangerous

- Uneven load distribution
- High latency on one shard
- System instability
- Cascading failures:
  - Retry storm
  - Cache stampede
  - Thundering herd

---

# Detection of Hot Keys

## A. Key-Level Metrics

Track:

- QPS per key
- Latency per key
- Error rate per key

Example:

```
product:123 → 50K QPS
product:456 → 200 QPS
```

---

## B. Shard-Level Metrics

Track:

- CPU usage
- Memory
- Latency (P95/P99)
- Queue depth

Example:

```
Shard 3 → 90% CPU
Shard 7 → 20% CPU
```

---

## C. Load Skew Formula

```
max_load / avg_load > threshold (e.g., 3x)
```

---

# Solution Idea: Key Splitting (Core Technique)

## Before

```
product:123 → shard 3
```

## After

```
product:123:1 → shard 2
product:123:2 → shard 7
product:123:3 → shard 1
```

- Load distributed across shards

---

# Two Main Strategies

---

## A. Replication (Read-heavy systems)

Same data need to be stored across multiple shards because each reach could go to any shard

### Writes:

```
SET product:123:1
SET product:123:2
SET product:123:3
```

### Reads:

```
GET product:123:{random(1..N)}
```

### Pros:

- Scales reads massively

### Cons:

- Write amplification
- Possible inconsistency
  - in case write to all shard fails

---

## B. Fragmentation (Write-heavy systems)

Split data across shards

### Writes:

```
INCR likes:post_1:1
INCR likes:post_1:2
INCR likes:post_1:3
```

### Reads:

```
SUM(likes:post_1:*)
```

### Pros:

- Scales writes

### Cons:

- Expensive reads
- Aggregation required

---

# Dynamic Shard Expansion

## Problem:

Initial:

```
shards = 3
```

Later:

```
traffic increases → need 10 shards
```

---

## Wrong Approach

```
hash(key) % N
```

Changing N breaks everything.

---

## Correct Approach: Indirection Layer

Metadata:

```json
{
  "product:123": {
    "shards": ["1", "2", "3"]
  }
}
```

---

## Scaling:

```json
{
  "product:123": {
    "shards": ["1", "2", "3", "4", "5", "6"]
  }
}
```

---

# Scaling Strategy Summary

| Problem          | Solution        |
| ---------------- | --------------- |
| Single hot key   | Key splitting   |
| Read-heavy       | Replication     |
| Write-heavy      | Fragmentation   |
| System-wide load | Add shards      |
| All combined     | Hybrid approach |
