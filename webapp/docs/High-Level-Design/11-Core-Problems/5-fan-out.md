# Fan-out on Write vs Fan-out on Read

---

## 🧠 Core Idea

- **Fan-out on Write** → Pay cost during write
- **Fan-out on Read** → Pay cost during read

👉 Trade-off: **Write Amplification vs Read Amplification**

## Fan-out on Write (Push Model)

### How it works

- When a user creates a post:
  - System pushes it to all followers’ feeds

- Feed is **precomputed and stored per user**

### Pros

- Fast read latency (feed already ready)
- Simple read queries

### Cons

- High write cost (1 post → millions of writes)
- Hot shard / partition issues
- Storage duplication (same post stored many times)
- Partial failures → inconsistent feeds

### Key Challenges

- Write amplification
  - overload
- Fault tolerance (retry, idempotency)
  - in case multiple write fails
- Ordering consistency
- Storage explosion

### Best Use Case

- Read-heavy systems
- Users with moderate follower count
- Low latency feed requirement

---

## Fan-out on Read (Pull Model)

### How it works

- Post stored once
- On feed request:
  - Fetch posts from all followed users
  - Merge + sort + rank

### Pros

- Cheap writes
- No duplication
- Scales well for high-follower users

### Cons

- Slow reads (aggregation required)
- Complex merging logic
- High CPU/memory usage
- Cache invalidation challenges

### Key Challenges

- Read amplification
- Merge & ranking complexity
- Pagination across multiple sources
- Cold start latency

### Best Use Case

- Write-heavy systems
- Users following fewer accounts
- Need for fresh data

## Hybrid Approach (Industry Standard)

### Strategy

- **Normal users → Fan-out on Write**
- **High-follower users → Fan-out on Read**

### Flow

- On post:
  - Normal → push to feeds
  - Celebrity → store once

- On read:
  - Fetch precomputed feed
  - Merge celebrity posts dynamically

---

## Advanced Concepts

### Async Fan-out

```
Post → Queue → Workers → Feed updates
```

### Consistency Model

- Eventual consistency is acceptable
- Slight delays in feed updates are normal

### Backpressure Handling

- Drop or delay low-priority updates
- Use queues to smooth spikes

---

## ⚖️ Quick Comparison

| Factor         | Fan-out on Write | Fan-out on Read |
| -------------- | ---------------- | --------------- |
| Write Cost     | High             | Low             |
| Read Cost      | Low              | High            |
| Latency (Read) | Fast             | Slower          |
| Scalability    | Hard (writes)    | Hard (reads)    |
| Storage        | High             | Low             |
