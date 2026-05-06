# Quorum Reads vs Writes and Replication Models

In distributed systems, ensuring consistency across multiple replicas of data is a core challenge. Two important concepts used to manage this are:

- **Quorum Reads (R)**
- **Quorum Writes (W)**

---

## Core Idea of Quorum

Assume:

- **N = Total number of replicas**
- **W = Number of replicas required to acknowledge a write**
- **R = Number of replicas queried during a read**

### Key Rule:

> To ensure strong consistency:

```
R + W > N
```

This guarantees that **at least one replica overlaps between read and write operations**, ensuring fresh data is returned.

---

## Quorum Writes (W)

A write is considered successful only when **W replicas acknowledge it**.

### Example:

- N = 3
- W = 2

**Flow:**

1. Client sends write request
2. System writes to 2 replicas
3. Once both acknowledge → write succeeds

### Trade-offs:

- ✅ Higher consistency
- ❌ Higher latency
- ❌ Lower availability if replicas are down

---

## Quorum Reads (R)

A read queries **R replicas** and returns the latest version.

### Example:

- N = 3
- R = 2

**Flow:**

1. Query 2 replicas
2. Compare versions/timestamps
3. Return the most recent value

### Trade-offs:

- ✅ Better freshness guarantees
- ❌ Higher latency
- ❌ Increased network overhead

---

## 5. Why R + W > N Works

Example:

- N = 3
- W = 2
- R = 2

Since reads and writes overlap on at least one node, the system can ensure that **reads observe the latest write**.

---

## Configuration Strategies

### Write-heavy systems

- W = N, R = 1
- Strong durability, fast reads

### Read-heavy systems

- W = 1, R = N
- Fast writes, slower but consistent reads

### Balanced

- W = 2, R = 2 (for N = 3)

---

## Important Nuances

### 1. Not Always Strong Consistency

Even if `R + W > N`, strong consistency is not guaranteed due to:

- Network delays
  - like at time of write node2 was slow but at read time node1 become slow so you read wrong value from node2
- Clock skew
- Asynchronous replication
  - in case event not processed or had some issue

### 2. Sloppy Quorum

- Writes go to any available nodes (if designated nodes are down)
- Improves availability
- Weakens consistency

### 3. Read Repair

- During reads, stale replicas are updated
- If the read quorum `R` detects that one node has stale data, the system asynchronously updates that node with the latest version from the other `R-1` nodes.

### 4. Hinted Handoff

- Temporary storage of writes for down nodes

### 5. Latency Optimization

- Systems may return early responses and reconcile later

### 6. Latency Tail

- Your request is only as fast as the slowest node in your quorum. In a `W=2` setup, if one node lags, your write latency spikes.

---

## Leader-Based Replication (Primary-Replica)

- One node acts as **leader (primary)**
- Others are **followers (replicas)**

### Flow

- Writes → Leader
- Reads → Leader or replicas

### Quorum Role

Quorum is not fundamental but appears in:

#### Write Durability

- Leader waits for acknowledgements from replicas
- Similar to W quorum

#### Read Consistency

- Reading from leader → strong consistency
- Reading from replicas → eventual consistency

### Characteristics

| Aspect              | Leader-Based              |
| ------------------- | ------------------------- |
| Write Path          | Single leader             |
| Conflict Resolution | Not required              |
| Consistency         | Strong (leader reads)     |
| Availability        | Lower (leader dependency) |
| Latency             | Write bottleneck          |

### Limitation

- Leader is a bottleneck
- Potential single point of failure

---

## Leaderless Replication (Dynamo-style)

- No leader
- Any node can accept reads/writes

### Flow

#### Write

- Sent to multiple replicas
- Success after W acknowledgements

#### Read

- Query R replicas
- Return latest version

### Why Quorum is Essential

- No single source of truth
- Multiple nodes accept writes
- Conflicts are inevitable

---

## Comparison

| Concept         | Leader-Based | Leaderless     |
| --------------- | ------------ | -------------- |
| Source of Truth | Leader       | Quorum         |
| Writes          | Single node  | Multiple nodes |
| Reads           | One node     | Multiple nodes |
| Conflicts       | Rare         | Common         |
| Quorum Usage    | Partial      | Fundamental    |

---

## When to Use

### Leader-Based

- Banking systems
- Transaction-heavy systems

### Leaderless

- Social media feeds
- Messaging systems
- IoT ingestion
