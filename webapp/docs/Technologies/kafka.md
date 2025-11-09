# Kafka

Apache Kafka is a **distributed streaming platform** designed for **high-throughput, fault-tolerant, real-time data streaming**.

At its core:

- Kafka is **a distributed commit log**.
- Data is written to **topics** and read by **consumers**.
- Each topic is **partitioned** and replicated across **brokers** in a **cluster**.

## 🧩 Core Concepts

### 1. **Cluster**

A Kafka cluster is a group of **brokers** (servers).
Each broker is identified by a unique ID and is responsible for storing **topic partitions**.

### 2. **Broker**

A broker:

- Stores **data partitions**.
- Serves producer and consumer requests.
- Broker having leader partition of a topic will act as a leader broker for that partition.
- Coordinates with other brokers using **ZooKeeper** (in older versions) or **KRaft mode** (in newer Kafka).

Each broker can act as a **leader** or **follower** for different partitions.

---

### 3. **Topic**

A topic is a **category or feed name** where messages are published.

Example:

```
orders, payments, user-activity
```

Topics are **split into partitions** for scalability.

---

### 4. **Partition**

A partition is an **ordered, immutable sequence of messages**.

Each message inside a partition has:

- A unique **offset** (like a sequence number)
- Key (optional)
- Value (the message payload)

#### Why Partitions?

They allow Kafka to:

- Scale horizontally (distribute data across brokers)
- Enable **parallel processing** by multiple consumers
  - Read for consumer is usually only allowed from leader partition so one consumer can read from one partition for a specific topic.
  - However ther can be paraller tcp connections opened for reading for 2 different topics.

---

### 5. **Replication**

Each partition has:

- **One leader replica**
- **One or more follower replicas**

The **leader replica** handles all read and write operations.
The **followers** continuously replicate data from the leader.

#### Example:

| Broker   | Partition Role          |
| -------- | ----------------------- |
| Broker 1 | Leader of Partition 0   |
| Broker 2 | Follower of Partition 0 |
| Broker 3 | Follower of Partition 0 |

## ⚙️ How Data Flows

### 1. **Producer → Broker**

When a producer sends a message:

1. Kafka’s **partitioner** decides which partition to write to.
   - Default: **Round-robin** (if no key)
   - Deterministic: **Based on key hash** (ensures same key → same partition)
2. The message is sent to the **leader broker** for that partition.
3. The leader **appends** it to the log and sends **ACK** once replication conditions are met.

---

### 2. **Broker → Replicas**

Each follower replica **pulls data** from its leader asynchronously.

If `acks=all`, the producer waits until:

- All **in-sync replicas (ISRs)** confirm replication,
- Then it considers the write successful.

### 3. **Consumer → Broker**

Consumers subscribe to a topic and read data **from the leader replicas** of each partition.

Kafka maintains **consumer offsets** (how much each consumer has read).

## 🧠 Kafka Architecture Interconnections

| Component             | Works With      | Description                           |
| --------------------- | --------------- | ------------------------------------- |
| **Producer**          | Broker (Leader) | Sends messages to partition leader    |
| **Broker**            | Other Brokers   | Replicates partitions, elects leaders |
| **Consumer**          | Broker          | Reads messages from partition leader  |
| **Controller Broker** | Cluster         | Manages metadata, leader elections    |
| **ZooKeeper/KRaft**   | Brokers         | Stores metadata, manages coordination |

## 🧩 What Happens When Things Fail

### 🧱 1. **Broker Failure**

- Each partition has replicas across brokers.
- If a broker fails:
  - The controller detects failure.
  - **A follower replica is promoted to leader**.
  - Producers and consumers are automatically redirected.

➡️ **Data Loss?**
If `min.insync.replicas` is properly configured and `acks=all`, no data is lost.

### 🧩 2. **Partition Leader Failure**

- The controller triggers **leader election** among follower replicas.
- A new leader is chosen from the **in-sync replicas**.
- Producers/consumers automatically start using the new leader.

### 🧩 3. **Producer Failure**

- If a producer crashes **before ACK**, the message may be re-sent (possible duplicates).
- Kafka provides **idempotent producers** to avoid duplication.
- Retries + idempotency ensure **exactly-once delivery**.

### 🧩 4. **Consumer Failure**

- Consumers belong to **consumer groups**.
- Each partition is consumed by only one consumer within a group.
- When a consumer dies:
  - A **rebalance** occurs.
  - Its partitions are reassigned to other consumers in the group.
- Offsets are stored in `__consumer_offsets` topic to allow **resume-from-last-point**.

## 🔁 Replication and Data Consistency

### Key Terms

- **Leader Replica:** Active partition serving reads/writes.
- **Follower Replica:** Passive copy syncing from leader.
- **In-Sync Replicas (ISR):** Replicas caught up with leader.

### Write Consistency

When a producer writes:

- Leader appends message → followers replicate → acks sent based on config.

| `acks` Config | Meaning                                           |
| ------------- | ------------------------------------------------- |
| `acks=0`      | Producer doesn't wait for acknowledgment          |
| `acks=1`      | Wait for leader acknowledgment only               |
| `acks=all`    | Wait for all ISR replicas to acknowledge (safest) |

## 🧮 Partition Assignment Logic

When a topic is created:

- You specify number of partitions and replication factor.
- Kafka’s **controller broker** assigns partitions to brokers using:
  - Load balancing (even distribution)

If a partition or broker fails → Kafka reassigns partitions dynamically.

## 🔐 Fault Tolerance Summary

| Failure Type              | Recovery Mechanism                              |
| ------------------------- | ----------------------------------------------- |
| **Broker Down**           | Controller elects new leader partitions         |
| **Leader Partition Down** | ISR replica becomes new leader                  |
| **Producer Down**         | Retries or idempotent producer ensures delivery |
| **Consumer Down**         | Consumer group rebalance reassigns partitions   |

## ⚡ Key Advantages

- **Durability** – messages are written to disk and replicated.
- **Scalability** – add brokers, partitions, consumers easily.
- **Fault-tolerance** – automatic failover and replication.
- **High-throughput** – optimized for sequential disk writes.

## 🧰 Practical Tips

- Use `acks=all` + `min.insync.replicas=2` for durability.
- Monitor ISR shrinkage → signals lagging replicas.
- Use **idempotent producers** and **transactional APIs** for exactly-once semantics.
- Scale consumers by **adding more partitions**.
- Avoid topics with too few partitions → limits parallelism.

## Usecases

- Real time data streaming
- Microservice communication (Event driven)
- Log Agreegator
- Data integration / ETL (Extract->Tranform->Load) pipelines
- Transactional messaging
  - exactly one delivery of atomic writes for financial systems

## Mind Map

```
Cluster
├── Brokers (Servers)
│ ├── Topics
│ │ ├── Partitions
│ │ │ ├── Replicas
│ │ │ └── Messages (Data)
│ │ └── ...
│ └── ...
├── Producers → write to topics (partitions)
├── Consumers → read from topics (partitions)
└── Coordination → metadata, replication, offsets, etc.
```

### CASE1: x topic with(replication:3, partition:2)

```
number of brokers should be alteast equal to replication we want

- Broker 1
  - part-x-1 - leader
  - part-x-2
- Broker 2
  - part-x-1
  - part-x-2 - leader
- Broker 3
  - part-x-1
  - part-x-2

here no leader in broker 3 as at a time one partition can only have 1 leader,
if one leader for any partition fail then partition from broker 3 will become leader

```

### CASE2: x topic with(replication:3, partition:3)

```
- Broker 1
  - part-x-1 - leader
  - part-x-2
  - part-x-3
- Broker 2
  - part-x-1
  - part-x-2 - leader
  - part-x-3
- Broker 3
  - part-x-1
  - part-x-2
  - part-x-3 - leader
```
