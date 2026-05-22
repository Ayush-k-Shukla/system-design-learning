# Temporal Architecture

## Core Problem Solved

- Building reliable distributed systems in modern microservices is complex. Only 20–30% of engineering effort goes to pure business logic. The remaining 70–80% is spent on defensive plumbing and boilerplate, including:
  - Managing database transaction states.
  - Handling flaky third-party network timeouts and scheduling backoff retry loops.
  - Maintaining complex state machines and distributed lock managers.
  - Writing manual database polling cron jobs to catch stuck processes.

- Temporal solves **the Distributed Systems Reliability problem** by providing **Durable Execution**. It hides infrastructure, letting developers write long-running distributed operations in normal languages as if they ran inside a single, crash-proof function.

---

## Core Concepts & Technical Philosophy

Temporal follows a **Code-over-Configuration** paradigm. Instead of JSON/YAML configs, it uses idiomatic code to define state machines.

### Workflows

A Workflow is an orchestration blueprint written in standard code (TypeScript, Go, Java, Python).

- **The Golden Rule:** Workflows **must be completely deterministic**. Because Temporal replays code to recover, running a workflow multiple times with the same inputs must yield the same execution path.
- **Banned Operations:** Direct API calls, database queries, reading system time (Date.now()), or generating random values (Math.random()) are not allowed inside a workflow.

### Activities

An Activity is a non-deterministic function where side effects occur. Put external API calls, DB queries, or disk I/O here. Activities can fail, time out, and retry independently of the workflow.

---

## High-Level System Architecture & Flow

Temporal uses an **Isolated Worker Model**. The Temporal Server is only the orchestrator, state manager, and queue coordinator — it never runs your code. Your code runs on your infrastructure (Kubernetes, EC2, etc.) inside a Worker process.

### Core Architecture Component Flow

<p align="center">
   <img src="/img/hld/temporal.png" />
</p>

---

## Operational Mechanics: The Code Implementation

### Defining Activities (activities.ts)

```typescript
import axios from 'axios';

export async function chargeCreditCard(
  amount: number,
  orderId: string,
): Promise<string> {
  const response = await axios.post(
    'https://api.payments.com/v1/charges',
    { amount, orderId },
    {
      headers: {
        // Idempotency key prevents double-charging during worker retry loops
        'Idempotency-Key': orderId,
      },
    },
  );
  return response.data.transactionId;
}

export async function updateDatabase(
  orderId: string,
  status: string,
): Promise<void> {
  console.log(`Updating DB for order ${orderId} to status: ${status}`);
}
```

### Defining the Deterministic Workflow (workflows.ts)

```typescript
import { proxyActivities } from '@temporalio/workflow';
import type * as activities from './activities';

// proxy intercepts calls and records intent to the Temporal Server
const { chargeCreditCard, updateDatabase } = proxyActivities<typeof activities>(
  {
    startToCloseTimeout: '1 minute',
    retryPolicy: {
      initialInterval: '2 seconds',
      backoffCoefficient: 2,
      maximumAttempts: 5,
    },
  },
);

export async function orderProcessingWorkflow(
  amount: number,
  orderId: string,
): Promise<string> {
  // `await` blocks until the activity reports success back to the server
  const txId = await chargeCreditCard(amount, orderId);

  // This line runs only after payment has cleared
  await updateDatabase(orderId, 'PAID');

  return `Order ${orderId} fully processed. Tx: ${txId}`;
}
```

---

## Architectural Q&A Deep Dive

### Q1: If the Temporal Server goes down, how does it know where a workflow left off when it restarts?

**Answer:** Temporal utilizes an immutable, append-only **Event History** database instead of taking traditional system state memory snapshots.

When a server reboots after a crash, it runs a process called **Replay**:

1. The recovered server assigns the incomplete workflow to an active Worker.
2. The Worker downloads the workflow's event history from the database.
3. The Worker re-executes the workflow code from the start.
4. When the code hits an `await`, the SDK checks the event log. If an entry exists showing the activity already completed, it suppresses the call and injects the recorded result.
5. This continues until the code reaches a point with no recorded event, resuming from that spot.

### Q2: If the system crashes mid-API call, how do we guarantee actions are not executed twice?

**Answer:** The protection depends on where the failure occurs:

- **Scenario A (Server Crash, Worker Alive):** The Worker receives HTTP 200 from Stripe but the server is down. The Worker buffers the response and retries the handshake until the server recovers. No duplicate call is made.
- **Scenario B (Worker Crash Mid-Flight):** The Worker dies before getting a response. The server uses `startToCloseTimeout` to mark the attempt failed and re-queues the activity for retry.
- **Counter-Measure:** Temporal can't undo outbound packets that already left the OS. For exact-once behavior, use **Idempotency Keys** (e.g., `orderId`). If a retry occurs, Stripe will ignore the duplicate key and return the original receipt.

### Q3: If I scale horizontally across multiple instances, does the workflow stay locked to the server that initiated it?

**Answer:** **No.** Temporal uses an elastic **Pull Model (queue-based)**, not hard routing to a host.

If Server A starts a workflow, it only triggers an event. The Matching Service enqueues the task. Any other server can pull and process the task. State lives in the event ledger, not on any host.

### Q4: Where does the Task Queue live, and how does Temporal ensure two workers don't grab the same task?

**Answer:** Task queues are virtual structures kept in the Matching Service and persisted in the database.

- To avoid two workers grabbing the same task, Temporal uses **Optimistic Concurrency Control (OCC)** and atomic row updates.
- Before sending a task, the server atomically updates the row from `QUEUED` to `RUNNING`:

  ```
  UPDATE tasks SET status = 'RUNNING', worker_id = 'Server-B'
      WHERE task_id = X
      AND
      status = 'QUEUED';
  ```

  - If Server B's transaction succeeds, it gets the task. Server C's concurrent update then fails the `WHERE status = 'QUEUED'` check and receives no payload, so it keeps polling.

## Temporal Use Cases

- **Subscription Billing Engines:** Managing users' 30-day billing cycles, handling grace periods, failed payment retries, and updating subscription tiers over months or years.
- **Complex Sagas / Distributed Transactions:** Booking a vacation that involves reserving a flight, a hotel, and a car rental. If the car rental fails, Temporal flawlessly executes custom compensating actions (refunds) for the flight and hotel.
- **CI/CD & Resource Provisioning:** Orchestrating the complex deployment pipelines of cloud platforms (HashiCorp uses Temporal heavily for Terraform Cloud).
- **Massive Distributed Cron Jobs:** Managing thousands of background jobs that require tracking state, complex retries, and dynamic scheduling.
