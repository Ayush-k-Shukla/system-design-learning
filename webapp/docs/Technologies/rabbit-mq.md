# 🐇 RabbitMQ

## 📘 Overview

RabbitMQ is a **message broker** that enables asynchronous communication between services. It uses the **AMQP (Advanced Message Queuing Protocol)** to send, route, store, and deliver messages between producers and consumers.

It’s like a **post office** — producers send messages to an exchange, which routes them to queues, and consumers receive from those queues.

---

## ⚙️ 1. Technical Working (Architecture & Flow)

### Components

- **Producer** → Sends messages to an exchange.
- **Exchange** → Routes messages to queues based on rules.
- **Queue** → Stores messages until consumed.
- **Consumer** → Receives and processes messages.
- **Binding** → Rule connecting an exchange to a queue.
- **Routing Key** → Address label for routing messages.

### Flow of a Message

1. Producer sends a message with a routing key to an **exchange**.
2. Exchange checks its **bindings** and routing logic.
3. Message is placed into one or more **queues**.
4. **Consumer(s)** receive messages from those queues.
5. Consumer sends **ACK** after successful processing.

### Exchange Types

| Type        | Behavior                             | Example Use Case     |
| ----------- | ------------------------------------ | -------------------- |
| **Direct**  | Routes by exact routing key match    | Logging system       |
| **Fanout**  | Broadcasts to all bound queues       | Notifications        |
| **Topic**   | Pattern-based routing with wildcards | Event-driven systems |
| **Headers** | Routes using message headers         | Complex filtering    |

---

### 🟢 Direct Exchange

Routes messages based on **exact routing key match**.

Example:

- Exchange: `logs` (type: direct)
- Bindings:
  - Queue A → key `error`
  - Queue B → key `info`

➡️ Message with key `error` → Queue A only.

---

### 🟠 Fanout Exchange

**Broadcasts** messages to all bound queues, ignoring routing key.

Example:

- Exchange: `broadcast`
- Queues: A, B, C (all bound)

➡️ Any message → A, B, C all receive.

---

### 🔵 Topic Exchange

Routes messages using **pattern matching** on routing key.

Wildcards:

- `*` → matches one word
- `#` → matches zero or more words

Example:

- Queue A bound to `order.*`
- Queue B bound to `order.#`

➡️ `order.created` → goes to both A & B.

---

### 🟣 Headers Exchange

Routes by **message headers** instead of routing keys.

Example:

- Queue A bound `{x-match=all, format=pdf, type=report}`
- Queue B bound `{x-match=any, format=pdf, type=log}`

➡️ Headers `{format=pdf, type=report}` → both A & B receive.

- `x-match=all` means asll header should match while `x-match=any` means any header match

---

## 🔗 2. Binding — The Glue

A **binding** connects an **exchange** to a **queue** and defines the routing rule.

You can bind:

- Many queues to one exchange ✅
- One queue to many exchanges ✅

For e.g.

```java
channel.queueBind("orderQueue", "orderExchange", "order.created");

// queue -> binding exchange -> routing key
```

---

## 🧠 3. Scaling & Trade-offs

### Why RabbitMQ Doesn’t Scale Horizontally Like Kafka

### ⚠️ Why RabbitMQ Doesn't Scale Horizontally Like Kafka

- **Single-Node Queue Ownership:** Every queue lives entirely on **one specific node**. Adding more nodes/brokers to your cluster does _not_ increase the capacity or speed of an existing queue—it is strictly limited by the hardware of its home node.
- **Scaling requires more Queues, not Brokers:** You cannot split a single queue across multiple machines. To scale throughput horizontally, you must create **multiple distinct queues** across different nodes, scale up your consumers, and manually shard your traffic (e.g., using a Consistent Hash Exchange).
- **Mirroring is for Resiliency, Not Performance:** Features like Quorum Queues provide **replication, not data distribution**. Because a single leader node still processes all reads/writes and must sync data across followers, mirroring increases network overhead and actually _decreases_ maximum throughput.

#### Quick Contrast

- **RabbitMQ:** Scales **vertically** (requires a larger node for a single queue) or requires complex application-side sharding.
- **Kafka:** Scales **horizontally** by default because a single topic is natively sliced into _partitions_ distributed across the entire cluster.

Kafka solves this with **partitioned topics**, enabling true horizontal scaling.

### Pros ✅

- Simple and reliable messaging.
- Supports multiple patterns (work queue, pub/sub, RPC).
- Fast with low latency.
- Mature ecosystem.

### Cons ❌

- Limited horizontal scaling.
- No message replay (unlike Kafka).
- Operational complexity in clusters.

---

## 🧭 4. Best Practices

- Use **durable queues** and **persistent messages** for reliability.
  - durable queues store message in disk as well not just on ram so it works well even queue node goes down
- Enable **manual acknowledgments** (`ACK`/`NACK`).
  - Avoid message loss
- Tune **prefetch count** for consumer load balancing.
  - it's like conmsumer will prefetch a amount of message so no oveload happens or message bombarding in peak time.
- Use **lazy queues** for large message backlogs.
  - Normally, RabbitMQ stores messages in RAM for speed.
  - But when queues get large (millions of messages), RAM fills up fast → system slows down.
  - To solve this, lazy queues store messages on disk by default, loading them into memory only when needed.

- Apply **TLS**, authentication, and **vhosts** for security.
- Avoid large payloads → store externally (e.g., S3) and send metadata.

[A good image](https://media.licdn.com/dms/image/v2/D5610AQHcoSLz3ao_9g/image-shrink_1280/B56ZsJJvDBIsAg-/0/1765385113787?e=1765990800&v=beta&t=S4sAxOVdQu2OrrwAKAIP2bjlEknSOstRtqZq1o4Fssk)
