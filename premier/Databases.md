# **Databases**

## Relational Database

1. Collection of data items organized in tables
2. **ACID Properties** fllowed by a Relational Database transaction (a transaction is a single unit of work or sometimes madeup of multiple transactions)
   1. **Atomicity**
      1. It ensures that a transaction is treated as a single unit.
      2. It means either all steps of transaction complete or none of them.
      3. If any part of transaction fails, the transaction is rolled back and the database is restored to the previous consistent state.
      4. How DB handles Atomicity
         1. Databases unses something called logs (write ahead log) which keeps tracks of all the changes made during an transaction.
         2. Steps
            1. Transaction started
            2. Operations will be performed in memory
            3. Transaction writes all the logs
            4. If all steps are successful, the transaction will commit and make the changes permanent
            5. if any fails then rollback will be performed using the Logs
   2. **Consistency**
      1. It means any transaction is moving databse from one consistent state to another consistent state.
      2. It takes care of all the constraints (uniqueness, foreign keys) to be satisfied before and after the transaction.
      3. If any rule fails transaction will not be committed.
      4. **e.g.** Consider a database with constraint account balance can't be negative. A transaction attempting to withdraw more money than available will fail the check so the transaction should be rolled back.
      5. **Consistency in Distributed Systems**
         1. Strong Consistency
            1. This guarantees that once a transaction is completed the latest data is available in all the nodes immediately.
         2. Eventual Consistency
            1. If a system prioritize availibility then data might take some time to be synchronize across multiple regions. Eventually all nodes will be consistent but in short term different nodes might see different version of data.
   3. **Isolation**
      1. It ensures that multiple transactions runs in isolation and dont effect other transaction.
      2. Isolation prevents data inconsistencies that can arise when 2 transactions access and update same data.
      3. Databases uses Locks (Shared, Exclusive) to ensure that Isolation.
      4. **Isolation levels**
         1. It comes with different isolation levels each having balance between performance and data integrity.
         2. **Read Uncommited**
            1. The lowest level where transactions can see uncomitted changes of each other
            2. Data corruption is high as the final value depends on the order of commit. Last one wins
            3. It is rarely used
            4. e.g. Transaction A updates `balance` to 1000 but doesn't commit yet. Transaction B can read the `balance` as 1000 eventhough it is not permanent yet.
         3. **Read Commited**
            1. A transaction can only read committed changes.
            2. It guarantees No Dirty Reads (when a transaction reads data that has been modified by another transaction, but not yet committed), and can be Non Repeatable Read (a transaction reads data, and then another transaction modifies or deletes that data before the first transaction complete.)
            3. Used in applications where exact consistency is less critical and performance is priority.
            4. e.g. TA reads `balance` as 1000 and continues and in between TB updates `balance` to 1002 so in second read TA will read inconsistent value.
         4. **Repeatable Read**
            1. Once a transaction reads data, It is locked until the transaction completes.
            2. This guarantees no dirty reads, No Non Repeatable Reads. Phantom reads (occurs when two same queries are executed, but the rows retrieved by the two, are different, bcs new row is added or existing got deleted by some transaction) may occur.
         5. **Serializable**
            1. Highest level of isolation.
            2. Transaction executed in a serializable manner, as if they run one after another.
            3. This gives no Dirty reads, No Non Repeatable Reads, and no phantom reads.
   4. **Durability**
      1. Once a transaction is committed, its changes are permanent even in the case of a crash.
      2. How it works
         1. This is achieved generally by writing data to a non-volatile storage (SSD, Hard drives, cloud storage) once the transaction is commited.
         2. Some things used here are WAL (write ahead log) and Checkpointing.
3. **Master slave replication**
4. **Master Master replication**
5. **Federation**
6. **Sharding**
7. **SQL Tuning**
8. **Denormalization**
