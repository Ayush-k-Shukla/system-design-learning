# 🔒 Locking (Normal and Distributed)

## Normal Locking (Single Node Locking)

In a single node system, locking is used when multiple threads try to access the critical section at the same time.

### Types

#### Optimistic

- The assumption is made that conflicts are rare and proceeds without locking.
- Before committing changes, it checks whether another process has modified the resource.
- It is a strategy where you read a record, take note of a version number, and check that the version hasn't changed before you write the record back.
- **Implementation:** Uses versioning to validate
- **Use case**
  - PostgreSQL uses Multi-Version Concurrency Control (MVCC)

#### Pessimistic

- Assumes that if something can go wrong, it will, so lock the resource before performing any operation.
- **Implementation:** Use DB row-level locks or thread sync primitives (`mutex`, `semaphore`).
- **Use case**
  - Banking system

## Distributed Locking (Multi Node Locking)

Distributed locking is a mechanism used to coordinate access to shared resources in a distributed system, where multiple nodes (services, processes, or servers) might try to modify the same data concurrently.

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

- **Advantages**

  - No need for explicit locks
  - Scales well in distributed systems

- **Disadvantages**

  - Not suitable for frequent locks
  - High contention can lead to many retries

#### Pessimistic

- Assumes conflicts are frequent and prevents other processes from modifying a resource while one process is working on it.
- Uses explicit locking to prevent concurrent modifications.
- Implemented using external distributed coordination systems like **Redis**, **ZooKeeper**, or **database locks**.

### Redis Based Distributed Locking

- **Working**
  - A process requests a lock from Redis using the `SET NX PX` command (Set if Not Exists + Expiry).
  - If the lock exists, other processes must **wait or retry**.
  - The process releases the lock after completing the operation.
- **Issues**
  - If a system crashes before releasing the lock, the resource remains locked.
  - Solution to above is using `Redlock Algo` whic used multiple nodes and work as a dirtributed locking mechanism.
- **Redlock Algorithm**
  - The process acquires a lock from at least `N/2 + 1` Redis nodes to confirm exclusivity.
  - If a node fails, the system still functions.
  - Ensures fault tolerance.

### Database Based Distributed Locking

- **Working**

  - Use a database table to store lock information.
  - A process inserts a row representing the lock.
  - Other processes wait until the row is released.

- **Implementation**

  ```sql
       -- Acquire lock
       INSERT INTO distributed_locks (lock_name, acquired_at)
       VALUES ('order_processing', NOW())
       ON CONFLICT DO NOTHING;

       -- Release lock
       DELETE FROM distributed_locks WHERE lock_name = 'order_processing';
  ```

- **Issues**
  - Deadlocks if locks are not released.
  - Performance bottleneck in high-concurrency scenarios.
  - Single point of failure if using a single database instance.

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
