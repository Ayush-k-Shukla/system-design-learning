# CRDT (Conflict-Free Replicated Data Types)

## What CRDTs Solve

CRDTs allow **multiple nodes to update shared data concurrently** without coordination, locks, or a master node.
They guarantee **Strong Eventual Consistency (SEC)** even with:

- Offline edits
- Network delays
- Duplicates
- Out‑of‑order messages
- Partitions

Useful for offline‑first apps, collaborative editing, distributed counters, edge systems, IoT, etc.

---

## CRDT Mathematical Foundations

CRDTs rely on three properties so all replicas converge without needing a global order.

### Commutative

Order of operations doesn’t matter.
`a ∘ b = b ∘ a`

### Associative

Grouping doesn’t matter.
`(a ∘ b) ∘ c = a ∘ (b ∘ c)`

### Idempotent

Applying same update multiple times gives same result.
`a ∘ a = a`

These guarantee deterministic convergence despite unreliable networks.

---

## Types of CRDTs

### State-based

- Replicas periodically send **entire state** (or delta state)
- Merge = deterministic function (e.g., max)
- Needs **eventual delivery** but tolerant to message loss
- Examples: G-Counter, PN-Counter, OR-Set

### Operation-based

- Replicas send **operations**: insert, delete, increment
- Requires reliable causal-broadcast (or deduplication)
- More compact than state-based

---

## CRDT Counters

### G-Counter (Grow Only Counter)

Used when only **increments** happen.

#### Structure

Each replica stores a number in a vector:

```
Replica A: [5, 0, 0]
Replica B: [0, 3, 0]
Replica C: [0, 0, 2]
```

#### Merge (max element-wise)

```
merge(A, B) = [5, 3, 0]
merge(result, C) = [5, 3, 2]
```

#### Final value

```
sum = 5 + 3 + 2 = 10
```

### PN-Counter (Positive-Negative Counter)

Supports **increments + decrements**.

#### Structure

Two G-Counters:

- P: increments
- N: decrements

#### Example

```
P = [5, 1]   // total increments
N = [2, 0]   // total decrements
```

#### Final value

```
value = sum(P) - sum(N) = 6 - 2 = 4
```

- Used in: Likes, IoT counts, quotas, rate limits

### Counter Correctness

- Counters are **eventually correct** if all updates eventually sync.
- During partitions, temporary differences are normal.

---

## Text CRDTs

Text CRDTs model documents as sequences of items.
Each insertion produces a unique identifier so replicas can converge.

Imagine 3 users editing the same text:

Initial text:

```
H E L L O
```

Two concurrent inserts:

- User A inserts "X" after position 2
- User B inserts "Y" after position 2

### Each insert given a unique ID:

```
A inserts X@idA
B inserts Y@idB
```

### Merge rule:

Sort by unique ID (timestamp + replica ID)

Example ordering:

```
idA < idB
```

Final text:

```
H E X Y L L O
```

All replicas converge.

---

## Yjs Internals (Most Practical Text CRDT Today)

### Item Structure

Each inserted character has:

- `id: { clientID, clock }`
- `left` reference
- `right` reference
- `content`

### Insert

Stores new item with references to neighbors.
Sorting by ID resolves conflicts.

### Delete

Marks item as deleted (tombstone), not removed.

### Merge / Sync

Operations are lightweight binary deltas.
Replicas converge deterministically.

### Used In

- Figma
- JupyterLab
- Notion-like apps
- VS Code collab plugins
- Next.js Live

---

## Real-World Use Cases of CRDTs

### Likes / Reactions (Facebook, Instagram)

- Distributed “Like” updates converge without double counting.

### IoT Counters

- Sensors increment local counters and sync later.

### Edge Rate Limiting

- Cloudflare-like distributed environments maintain usage totals globally.

### Collaborative Text Editing

- Google Docs-style editing without a central lock.

### Distributed Quotas (API calls, storage limits)

- Global usage sums converge even across regions.
