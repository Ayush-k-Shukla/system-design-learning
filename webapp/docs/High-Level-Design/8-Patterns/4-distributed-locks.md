# 🔒 Locking (Normal and Distributed)

## Normal Locking (Single Node Locking)

In a single node system, locking is used when multiple threads try to access the critical system at the same time.

### Types

1. **Optimistic**
   1. The assumption is made that conflicts are rare and proceeds without locking.
   2. Before committing changes, it checks whether another process has modified the resource.
   3. It is a strategy where you read a record, take note of a version number, and check that the version hasn't changed before you write the record back.
   4. **Implementation:** Uses versioning to validate
   5. **Use case**
      1. PostgreSQL uses Multi-Version Concurrency Control (MVCC)
2. **Pessimistic**
   1. Assumes that if something can go wrong, it will, so lock the resource before performing any operation.
   2. **Implementation:** Use DB row-level locks or thread sync primitives (`mutex`, `semaphore`).
   3. **Use case**
      1. Banking system

## Distributed Locking (Multi Node Locking)

Distributed locking is a mechanism used to coordinate access to shared resources in a distributed system, where multiple nodes (services, processes, or servers) might try to modify the same data concurrently.

### Types

1. **Optimistic**

   1. The assumption is made that conflicts are rare and allows multiple nodes to proceed with an operation without explicit locking.
   2. **Implementation:**

      ```
          -- Step 1: Read the record
          SELECT id, value, version FROM items WHERE id = 1;

          -- Step 2: Try updating the record with version check
          UPDATE items
          SET value = 'newValue', version = version + 1
          WHERE id = 1 AND version = 3; -- Only updates if version is still 3
      ```

   3. **Use case**

      1. NoSQL databases (Cassandra, DynamoDB, etc.) use versioning for concurrent updates.
      2. E-commerce applications where multiple users try to purchase limited stock.
      3. API rate limiting (checking counters with atomic operations).

   4. **Advantages**

      1. No need for explicit locks
      2. Scales well in distributed systems

   5. **Disadvantages**

      1. Not suitable for frequent locks
      2. High contention can lead to many retries

2. **Pessimistic**

   1. Assumes conflicts are frequent and prevents other processes from modifying a resource while one process is working on it.
   2. Uses explicit locking to prevent concurrent modifications.
   3. Implemented using external distributed coordination systems like **Redis**, **ZooKeeper**, or **database locks**.

### Redis Based Distributed Locking

1. **Working**
   1. A process requests a lock from Redis using the `SET NX PX` command (Set if Not Exists + Expiry).
   2. If the lock exists, other processes must **wait or retry**.
   3. The process releases the lock after completing the operation.
2. **Issues**
   1. If a system crashes before releasing the lock, the resource remains locked.
   2. Solution to above is using `Redlock Algo` whic used multiple nodes and work as a dirtributed locking mechanism.
3. **Redlock Algorithm**
   1. The process acquires a lock from at least `N/2 + 1` Redis nodes to confirm exclusivity.
   2. If a node fails, the system still functions.
   3. Ensures fault tolerance.

### Database Based Distributed Locking

1. **Working**

   1. Use a database table to store lock information.
   2. A process inserts a row representing the lock.
   3. Other processes wait until the row is released.

2. **Implementation**

   ```
        -- Acquire lock
        INSERT INTO distributed_locks (lock_name, acquired_at)
        VALUES ('order_processing', NOW())
        ON CONFLICT DO NOTHING;

        -- Release lock
        DELETE FROM distributed_locks WHERE lock_name = 'order_processing';

   ```

3. **Issues**

   1. Deadlocks if locks are not released.
   2. Performance bottleneck in high-concurrency scenarios.
   3. Single point of failure if using a single database instance.

### ZooKeeper-Based Distributed Locking

Apache **ZooKeeper** is a **highly available distributed coordination system** used for leader election, service discovery, and distributed locking. It provides **strong consistency** and is widely used in large-scale systems.

1. **Working**

   1. ZooKeeper uses a concept called **znodes** (ZooKeeper nodes) for storing data in a hierarchical structure, similar to a filesystem.
   2. **Step 1: Clients Create Ephemeral Sequential znodes**

      1. Each process creates a **znode** under the `/locks` directory.
      2. | Process | Znode Created  | Znode Path            |
         | ------- | -------------- | --------------------- |
         | **P1**  | `lock-0000001` | `/locks/lock-0000001` |
         | **P2**  | `lock-0000002` | `/locks/lock-0000002` |
         | **P3**  | `lock-0000003` | `/locks/lock-0000003` |
      3. Sequential znodes ensure an ordering mechanism.
      4. Ephemeral znodes disappear if the client disconnects (prevents deadlocks).

   3. **Step 2: Finding the Process with the Smallest znode**

      1. Each process lists all znodes under `/locks`.
      2. The process with the smallest number (`lock-0000001`) gets the lock.
      3. **P1 acquires the lock** and starts writing to the file.

   4. **Step 3: Other Processes Watch the Next Smallest Znode**

      1. P2 watches **`lock-0000001`**.
      2. P3 watches **`lock-0000002`**.

   5. **Step 4: Lock Release and Next Process Acquires It**

      1. Once P1 finishes, it deletes `/locks/lock-0000001`.
      2. P2 now acquires the lock and starts writing.

2. **Advantages**

   1. **Strong Consistency:** Only one process gets the lock at a time.
   2. **Automatic Deadlock Prevention:** Ephemeral znodes **auto-delete** if a process crashes.
   3. **Scalability:** Works in **distributed systems** with multiple nodes.
   4. **Sequential Ordering:** Ensures **fair queuing** of lock requests.

3. **Challenges:**

   1. Requires a **ZooKeeper quorum** (at least **3 nodes**) for high availability.
   2. **More overhead** than Redis locks due to disk persistence.

4. **Usecase**

   1. **Leader election** in distributed systems.
   2. **Ensuring exclusive access** to shared resources.
   3. **Ensuring correct order of execution** in queue-based systems (Kafka, Hadoop, etc.).
   4. **Critical operations where consistency is a must** (e.g., distributed databases).
