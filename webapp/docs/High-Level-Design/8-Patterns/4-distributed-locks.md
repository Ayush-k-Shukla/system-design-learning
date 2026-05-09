# 🔒 Locking (Normal and Distributed)

## Normal Locking (Single Node Locking)

In a single node system, locking is used when multiple threads try to access the critical section at the same time.

### Types

#### Optimistic

- The assumption is made that conflicts are rare and proceeds without locking.
- Before committing changes, it checks whether another process has modified the resource.
- It is a strategy where you read a record, take note of a version number, and check that the version hasn't changed before you write the record back.
- **Implementation:** Uses versioning or timestamps to validate.
- **Use case**
- PostgreSQL uses Multi-Version Concurrency Control (MVCC).

#### Pessimistic

- Assumes that if something can go wrong, it will, so lock the resource before performing any operation.
- **Implementation:** Use DB row-level locks or thread sync primitives (`mutex`, `semaphore`).
- **Use case**
- Banking systems (e.g., preventing double-spending during a transfer).

---

## Distributed Locking (Multi Node Locking)

Distributed locking is a mechanism used to coordinate access to shared resources in a distributed system, where multiple nodes (services, processes, or servers) might try to modify the same data concurrently.

### The Lease Mechanism (Lease Expiry)

In distributed systems, we cannot rely on a process to always release a lock (it might crash or lose network). We use **Leases**.

- **Lease Expiry (TTL):** Every lock is granted with a Time-To-Live (TTL). If the process doesn't release the lock or renew it within this time, the lock is automatically revoked.
- **Watchdog / Renewal Pattern:** To prevent a lock from expiring while a long-running task is still healthy, a background thread (Watchdog) periodically "heartbeats" or extends the lease.
- **Fencing Tokens:** To solve the "Stop-the-World" problem (where a process wakes up after a long GC pause thinking it still has the lock), the locker provides a **monotonically increasing ID**. The resource (e.g., storage) rejects any request with a token smaller than the last one it processed.

### Types

#### Optimistic

- The assumption is made that conflicts are rare and allows multiple nodes to proceed with an operation without explicit locking.

```sql
-- Step 1: Read the record
SELECT id, value, version FROM items WHERE id = 1;

-- Step 2: Try updating the record with version check
UPDATE items
SET value = 'newValue', version = version + 1
WHERE id = 1 AND version = 3; -- Only updates if version is still 3

```

- **Use case**
- NoSQL databases (Cassandra, DynamoDB, etc.) use versioning for concurrent updates.
- E-commerce applications where multiple users try to purchase limited stock.
- API rate limiting (checking counters with atomic operations).

#### Pessimistic

- Assumes conflicts are frequent and prevents other processes from modifying a resource while one process is working on it.
- Implemented using external distributed coordination systems like **Redis**, **ZooKeeper**, or **Etcd**.

---

### Redis Based Distributed Locking

- **Working**
- A process requests a lock from Redis using `SET resource_name my_unique_id NX PX 30000`.
  - `NX`: Set if Not Exists (Mutual Exclusion).
  - `PX 30000`: Sets a **Lease** of 30 seconds (Automatic Expiry).

- **Renewal:** Libraries like Redisson use a "Watchdog" to extend the 30s lease as long as the process is alive.

- **Issues**
  - **Clock Drift:** If system clocks between Redis nodes are not synchronized, the lease might expire "early" on one node.

  - **Redlock Algorithm**
    - The process acquires a lock from a majority (`N/2 + 1`) of independent Redis nodes.
    - It considers the lock "held" only if it can acquire the majority within a small fraction of the total lease time.

---

### Database Based Distributed Locking

- **Working**
- Use a database table to store lock information.
- **Lease Implementation:** The table must include an `expires_at` column.

- **Implementation**

```sql
  -- Acquire lock with lease
  INSERT INTO distributed_locks (lock_name, acquired_at, expires_at, token)
  VALUES ('order_processing', NOW(), NOW() + INTERVAL '30 seconds', 101)
  ON CONFLICT (lock_name) DO UPDATE
  SET acquired_at = NOW(), expires_at = NOW() + INTERVAL '30 seconds'
  WHERE distributed_locks.expires_at < NOW(); -- Re-acquire only if expired

  -- Release lock
  DELETE FROM distributed_locks WHERE lock_name = 'order_processing';

```

- **Issues**
- Polling the DB for lock availability creates high I/O load.
- Requires a "cleanup" job to delete old, expired rows.

---

### ZooKeeper-Based Distributed Locking

Apache **ZooKeeper** is a **highly available distributed coordination system** used for leader election, service discovery, and distributed locking. It provides **strong consistency** and is widely used in large-scale systems.

- **Working**
  - ZooKeeper uses a concept called **znodes** (ZooKeeper nodes) for storing data in a hierarchical structure, similar to a filesystem.
  - **Step 1: Clients Create Ephemeral Sequential znodes**
    - Each process creates a **znode** under the `/locks` directory.
    - | Process | Znode Created  | Znode Path            |
      | ------- | -------------- | --------------------- |
      | **P1**  | `lock-0000001` | `/locks/lock-0000001` |
      | **P2**  | `lock-0000002` | `/locks/lock-0000002` |
      | **P3**  | `lock-0000003` | `/locks/lock-0000003` |
    - Sequential znodes ensure an ordering mechanism.
    - Ephemeral znodes disappear if the client disconnects (prevents deadlocks).

  - **Step 2: Finding the Process with the Smallest znode**
    - Each process lists all znodes under `/locks`.
    - The process with the smallest number (`lock-0000001`) gets the lock.
    - **P1 acquires the lock** and starts writing to the file.

  - **Step 3: Other Processes Watch the Next Smallest Znode**
    - P2 watches **`lock-0000001`**.
    - P3 watches **`lock-0000002`**.

  - **Step 4: Lock Release and Next Process Acquires It**
    - Once P1 finishes, it deletes `/locks/lock-0000001`.
    - P2 now acquires the lock and starts writing.

- **Advantages**
  - **Strong Consistency:** Only one process gets the lock at a time.
  - **Automatic Deadlock Prevention:** Ephemeral znodes **auto-delete** if a process crashes.
  - **Scalability:** Works in **distributed systems** with multiple nodes.
  - **Sequential Ordering:** Ensures **fair queuing** of lock requests.

- **Challenges:**
  - Requires a **ZooKeeper quorum** (at least **3 nodes**) for high availability.
  - **More overhead** than Redis locks due to disk persistence.

- **Usecase**
  - **Leader election** in distributed systems.
  - **Ensuring exclusive access** to shared resources.
  - **Ensuring correct order of execution** in queue-based systems (Kafka, Hadoop, etc.).
  - **Critical operations where consistency is a must** (e.g., distributed databases).
