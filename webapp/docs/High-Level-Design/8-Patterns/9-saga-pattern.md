# The Saga Pattern and 2PC and Distributed Transaction

## ACID Transactions in Monolithic Applications

In a traditional monolithic architecture, business use cases span multiple database tables but operate within a **single local database transaction**.

### Characteristics of Monolithic Transactions:

- **Unified Scope:** The transaction encapsulates all updates (e.g., updating both `Customer` and `Order` tables simultaneously).
- **All-or-Nothing Execution:** If any single update fails midway through execution, the database management system (DBMS) automatically rolls back the entire operations block to its initial, pre-transaction state.
- It follws **ACID** framework
- **Developer Simplicity:** Developers benefit from exclusive, isolated database access, minimizing complex conflict-resolution logic.

<p align="center">
    <img src="/img/hld/mono-db.webp"/>
</p>
---

## Distributed Transactions & The Two-Phase Commit (2PC) Pattern

Microservices architectures decentralize data, dedicating a standalone database to each microservice. This structural isolation breaks monolithic ACID guarantees, making traditional atomic changes impossible when workflows span multiple service domains.

One classic strategy for distributed consistency is the **Two-Phase Commit (2PC)** pattern, which coordinates resource updates across multiple network nodes in a single atomic transaction.

### The Two Phases of 2PC:

1.  **Prepare Phase:** A central coordinator queries all participating nodes to verify if they can successfully execute the requested update.
    - Each node checks its local state, reserves necessary resources/locks, and replies with either a "Vote to Commit" or a "Vote to Abort".
    - If any single node indicates it cannot proceed, the coordinator issues a global rollback command to all nodes, releasing all held locks.
2.  **Commit Phase:** If all participating nodes vote successfully in the prepare phase, the coordinator broadcasts a command to execute the update.
    - All nodes commit their changes permanently and release their local resources/locks.

<p align="center">
    <img src="/img/hld/2pc.webp"/>
</p>

### Drawbacks & Limitations of 2PC in Microservices:

- **Synchronous Blocking Architecture:** 2PC enforces strict, synchronous strong consistency. Participating nodes must lock target data objects continuously from the beginning of the Prepare phase until the final Commit phase completes. This creates severe performance bottlenecks and increases the probability of **deadlock situations**.
- **Lack of Modern Ecosystem Support:** Many widely adopted cloud-native storage engines and message brokers do not natively support 2PC protocols.
  - _Unsupported Databases:_ NoSQL systems like **Cassandra**.
  - _Unsupported Message Brokers:_ Distributed stream platforms like **Apache Kafka** and **RabbitMQ**.

---

## The Saga Pattern

To address the blocking and scaling constraints of 2PC, modern microservices architectures utilize the **Saga Pattern**.

<p align="center">
    <img src="/img/hld/basic-saga.webp"/>
</p>

### Core Concept

> **Definition:** A pattern that maintains data consistency across boundary-separated services using a coordinated sequence of local transactions driven by asynchronous messaging.

Instead of a single global transaction encompassing all services, a Saga breaks down the distributed workflow into a series of distinct **local transactions**.

### How It Operates:

1.  Each individual service participant performs a local transaction using familiar, local ACID frameworks.
2.  Upon completing its local transaction, the service updates its database and publishes an asynchronous event/message.
3.  This event triggers the next participant in line to execute its corresponding local transaction.

```
[Local Transaction 1] -> Updates DB 1 -> Publishes Event 1
                                             │
                       ┌─────────────────────┘
                       ▼
[Local Transaction 2] -> Updates DB 2 -> Publishes Event 2 ...
```

### Handling Failures (Compensating Transactions)

If a local transaction encounters a business violation or system error mid-Saga, atomicity cannot be achieved via standard database rollbacks because preceding transactions have already permanently committed to their respective databases.

Instead, the Saga executes a series of explicitly designed **compensating transactions**. These are "undo" operations executed in reverse order to programmatically revert the data changes introduced by the preceding successful steps, restoring the system to a clean, consistent state.

### Key Trade-Offs of the Saga Pattern

- **Advantages:**
  - **Asynchronous & Eventually Consistent:** Operates asynchronously, highly aligning with decentralized microservices architectures.
  - **High Availability:** Asynchronous message queues ensure that even if a specific service participant is temporarily offline, the Saga steps remain queued and will execute reliably once the service recovers.
  - **Support for Long-Lived Transactions:** Executes long, complex business workflows across domains without holding blocking database locks on shared records.
- **Weaknesses & Challenges:**
  - **Isolation Absence:** There is no structural isolation between concurrent Sagas; uncommitted data changes are immediately visible to outside operations.
  - **Complex Error Recovery:** Programmatically defining, testing, and ensuring the reliable execution of compensating transactions adds notable complexity.

---

## Saga Coordination Mechanisms

Sagas are structured into two main architectural strategies based on how their execution steps are coordinated: **Choreography-based** and **Orchestration-based**.

### A. Choreography-Based Sagas

In a Choreography model, there is **no centralized coordinator** or single point of control. Instead, the workflow emerges naturally as participants subscribe to and act upon each other's published events.

- **Interaction Model:** Asynchronous Publisher/Subscriber (Pub/Sub) Messaging.
- **Workflow Execution:** Service A executes its local transaction and publishes an event to a specific message topic.
  - Service B and Service C subscribe to that topic. Upon consuming the event, they trigger their internal logic, execute local transactions, and subsequently publish their own downstream events to the broker.
  - The process repeats sequentially until the workflow concludes.

<p align="center">
    <img src="/img/hld/saga-choreo.webp"/>
</p>

#### Architectural Benefits:

- **Simplicity:** Highly intuitive for simple, direct workflows involving a small number of steps.
- **Loose Coupling:** Participants rely exclusively on messaging topics and remain unaware of the internal topologies or specific implementations of other services.

#### Architectural Drawbacks:

- **Obscured Operational Flow:** Because the business flow logic is scattered across multiple independent service codebases, it is highly difficult to understand, trace, or map the entire distributed transaction from a holistic perspective.
- **Cyclic Dependencies:** There is an inherent risk of creating closed looping logic or cyclic dependencies where services inadvertently trigger one another in circles (e.g., `Service 01` -> `Service 02` -> `Service 01`).
- **Tight Event-Level Coupling:** Services may become tightly coupled to the granular structure and existence of events published by multiple other systems just to manage their own state.

### B. Orchestration-Based Sagas

In an Orchestration model, coordination logic is centralized within a dedicated component called the **Saga Orchestrator**. The orchestrator acts as a conductor, directly commanding individual participants on which local transactions to execute.

- **Interaction Model:** Asynchronous Request/Response driven by **Command Messages**.
- **Workflow Execution:**
  - An initial request triggers a microservice, which instantiates a specialized Saga Orchestrator class.
  - The microservice marks the incoming request state as `PENDING` (e.g., `APPROVAL_PENDING`).
  - The Saga Orchestrator sends targeted, asynchronous command messages over dedicated request channels to specific participants.
  - Participants execute their local transactions and reply with success or failure messages back to the orchestrator via a centralized response channel.
  - The orchestrator evaluates the response payload and determines the next step: either issuing the next sequential command or initiating the compensating transaction chain.

<p align="center">
    <img src="/img/hld/saga-orche.webp"/>
</p>

#### Architectural Benefits

- **Simpler Dependency Graph:** The orchestrator systematically invokes participants, but participants never invoke the orchestrator or each other. This structurally eliminates cyclic dependencies.
- **Reduced Component Coupling:** Individual microservices merely execute incoming commands and emit standard responses. They do not need to know about the overarching business workflow, events published by other services, or adjacent domain logic.
- **Centralized Control Flow:** The entire state machine and transactional layout are defined within a single orchestrator component, greatly simplifying system comprehension, debugging, and maintenance.

#### Architectural Drawbacks

- **Risk of Bloated Orchestrators:** Developers face the anti-pattern risk of accidentally centralizing core business logic inside the orchestrator class. The orchestrator must strictly manage _coordination and state transition sequences only_, leaving domain-specific business logic enclosed within the respective participant microservices.
- **Lack of Transactional Isolation:** Because each participant instantly commits its local changes to a separate database before the entire Saga finishes, the architecture lacks global isolation, giving rise to potential concurrent anomalies.

---

## Saga Isolation Anomalies & Countermeasures

The lack of structural isolation in the Saga pattern means that concurrent transactions can read or overwrite uncommitted data states, exposing systems to three distinct anomalies:

> when referring "another Saga instance" or "another concurrent Saga," it is referring to a completely separate, independent execution of a business workflow running at the exact same time.

1.  **Lost Updates:** Occurs when one Saga instance accidentally overwrites an uncommitted data update made by an independent, concurrently running Saga instance.
2.  **Dirty Reads:** Occurs when a Saga reads intermediate, uncommitted data modifications while those changes are in the middle of being processed by another concurrent Saga. If the first Saga fails and rolls back via compensation, the second Saga will have operated on invalid, phantom data.
3.  **Fuzzy / Non-Repeatable Reads:** Occurs when different steps within a single Saga read the exact same data record but obtain different values because an unrelated, concurrent Saga updated that record in the interim.

_Note: In production environments, Lost Updates and Dirty Reads represent the most frequent and severe anomalies._

### Practical Countermeasures to Mitigate Anomalies:

- **Semantic Lock:** An application-level locking mechanism where a compensable transaction flags a record to specify that it is uncommitted and subject to change (e.g., setting a status field to `APPROVAL_PENDING` or `REVISION_PENDING`). Other transactions reading this record can detect the flag and adapt their behavior accordingly until the lock is cleared by a final retriable or compensating transaction.
- **Commutative Updates:** Designing state modification operations to be strictly commutative—meaning updates can execute in any arbitrary order without altering the final mathematical outcome (e.g., debiting and crediting balances). This approach effectively neutralizes the risk of Lost Updates.
- **Reread Values:** A validation strategy where a service rereads a target record immediately before performing an update to ensure that it has remained completely unchanged during the process. If changes are detected, the update aborts or the Saga restarts, preventing a Lost Update.

---

## Architectural Summary Guide

To assist in pattern selection, use the following structural comparison as a design guideline:

| Feature/Metric          | ACID Transactions        | Two-Phase Commit (2PC)     | Choreography Saga               | Orchestration Saga                |
| :---------------------- | :----------------------- | :------------------------- | :------------------------------ | :-------------------------------- |
| **Consistency Mode**    | Strong Consistency       | Strong Consistency         | Eventual Consistency            | Eventual Consistency              |
| **Execution Model**     | Synchronous              | Synchronous Blocking       | Asynchronous                    | Asynchronous                      |
| **Central Coordinator** | Database Engine          | Yes (Transaction Manager)  | No                              | Yes (Saga Orchestrator)           |
| **Communication Style** | Internal DB Engine Calls | Direct Network Protocol    | Pub/Sub Events                  | Command Messages / Responses      |
| **Isolation Level**     | High (Built-in DBMS)     | High (Data Objects Locked) | None (Requires Countermeasures) | None (Requires Countermeasures)   |
| **Best Suited For**     | Monoliths / Single DB    | Minimal nodes, legacy DBs  | Simple flows (few services)     | Complex workflows (many services) |

---
