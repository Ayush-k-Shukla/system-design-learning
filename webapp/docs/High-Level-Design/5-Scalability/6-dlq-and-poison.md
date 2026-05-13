# Poison Messages & Dead Letter Queues (DLQ)

- **Poison Message:** A message that is technically valid in format but causes a consumer to fail consistently due to logic errors, schema mismatches, or unresolvable dependencies.
- **Dead Letter Queue (DLQ):** A "quarantine" service/queue where messages are moved after exceeding a maximum retry threshold. It prevents **Head-of-Line (HoL) blocking** and "Retry Storms."

---

### The Failure Lifecycle

- **Transient Failure:** Short-term glitches (network jitter).
  - _Solution:_ **In-Memory Retry.** Fast, but risks blocking the consumer heartbeat.

- **Persistent Failure:** Longer outages (DB down for 5 mins).
  - _Solution:_ **Retry Topics (Delayed).** Message is published to a secondary topic with a "backoff" period, allowing the main consumer to continue.

- **Permanent Failure (Poison):** Data is fundamentally broken.
  - _Solution:_ **DLQ.** After `N` total attempts across all tiers, the message is moved here for manual inspection.

---

### DLQ handeling Implementation: SQS vs. Kafka

| Feature        | **Traditional (SQS/RabbitMQ)**                     | **Event Streaming (Kafka)**                                        |
| -------------- | -------------------------------------------------- | ------------------------------------------------------------------ |
| **Mechanism**  | **Push-based.** Broker manages the move.           | **Log-based.** Consumer must "Copy & Commit."                      |
| **Storage**    | **Physical Move.** Message is deleted from source. | **Immutability.** Message stays in log; copy is sent to DLQ topic. |
| **Automation** | Highly automated via broker config.                | Manual. Developer writes code to produce to DLQ.                   |
| **Ordering**   | Can easily break strict ordering.                  | Easier to maintain via partition logic.                            |

---

### Critical Nuances

- **The "Commit" Requirement (Kafka):** In Kafka, moving a message to a DLQ is a two-step atomic-like operation: You must **Produce** the message to the DLQ topic and then **Commit the Offset** on the main topic. If you don't commit, the consumer stays stuck on the poison message.
- **Observability:** A DLQ is useless if it isn't monitored. Always mention attaching **Alerting (Threshold-based)** to the DLQ depth.
- **The Redrive Pattern:** How do you get messages out of the DLQ?
  - _Fix the code_ (if it was a bug).
  - _Fix the data_ (if it was a poison message).
  - _Re-ingest_ (moving messages back to the main topic).
- **Idempotency:** Because retries and DLQ redrives can result in the same message being processed twice, the consumer **must** be idempotent (e.g., using a `request_id` or `deduplication_key` in the DB).
- Always distinguish between **Delivery Failure** (Producer -> Broker) and **Processing Failure** (Broker -> Consumer). DLQs specifically solve the latter.
