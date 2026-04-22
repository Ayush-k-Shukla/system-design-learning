# Out-of-Order Event Processing

- Out-of-order event processing occurs when events are processed in a different order than they were generated.

Example:

```
Generated: E1 → E2 → E3
Processed: E2 → E1 → E3
```

---

## Why It Happens

1. Network delays
2. Parallel processing
3. Partitioning across systems
4. Retries and failures
5. Clock skew
   1. diff time readings between machines

---

## Where It Matters

- Financial systems
- Inventory systems
- Event-driven architectures
- Real-time analytics

---

# Handling Strategies (Deep Dive)

---

## 1. Buffering + Reordering

- Hold events temporarily, sort by timestamp, then process.

### Working

- Maintain buffer (priority queue / min-heap)
- Track max_seen_timestamp
- Define allowed lateness

Process condition:

```
event.timestamp <= max_seen_timestamp - allowed_lateness
```

### Edge Cases

- Infinite waiting → use bounded lateness
- Memory growth → limit buffer or spill to disk

### Trade-offs

- High accuracy
- Increased latency
- High memory usage

### Use Cases

- Analytics pipelines
- Log processing

---

## 2. Watermarks

- A watermark represents a point in time after which no older events are expected.
- kind of a version of buffer with hard limit

### Working

If watermark = T:

```
Process all events where timestamp <= T
```

Late events:

- Dropped
- Sent to side output
- Handled separately

### Types

- Fixed watermark: max_seen_timestamp - delay
- Heuristic watermark
- Punctuated watermark

### Trade-offs

- Controlled latency
- Configurable accuracy
- Medium complexity

### Use Cases

- Windowed aggregations
- Streaming systems

---

## 3. Idempotency

- Ensure processing an event multiple times does not change the result.

### Working

- Assign unique event_id
- Maintain processed_events store

```
if event_id already processed → skip
```

### Patterns

- Deduplication store (Redis/DB)
- Upserts
- Idempotent writes (set absolute state)

### Limitation

Does NOT solve ordering issues.

### Trade-offs

- Simple
- Extra storage required

### Use Cases

- Payments
- APIs

---

## 4. Versioning / Sequence Numbers

- Each event has a monotonically increasing version.

### Working

Maintain current_version:

```
if incoming_version > current_version → apply
else → ignore
```

### DB Pattern

```
UPDATE table
SET value = X, version = v
WHERE version < v
```

### Edge Cases

- Missing events may be skipped forever

### Trade-offs

- High correctness
- Possible data loss

### Use Cases

- Profile updates
- State sync

---

## 5. State Reconciliation (Event Sourcing)

- Recompute correct state periodically from event log.

### Working

- Store all events (append-only log)
- Periodically:
  - Sort events
  - Recompute state

### Trade-offs

- Eventually correct
- High compute cost
- High latency

### Use Cases

- Financial ledgers
- Auditing systems

---

## 6. Partitioning Strategy

- Ensure related events go to same partition to preserve order.

### Working

- Partition by key (userId, orderId)
- Ordering guaranteed within partition

### Problems

- Hot partitions
- No global ordering

### Trade-offs

- High scalability
- Partial ordering only

### Use Cases

- Messaging systems
- Event streaming

---

# Final Mental Model

## Layer 1: Prevent

- Partitioning

## Layer 2: Control

- Buffering
- Watermarks

## Layer 3: Protect

- Idempotency
- Versioning

## Layer 4: Correct

- Reconciliation
