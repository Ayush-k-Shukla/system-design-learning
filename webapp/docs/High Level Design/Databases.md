# Databases

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
            2. It guarantees No **Dirty Reads** (when a transaction reads data that has been modified by another transaction, but not yet committed), and can be **Non Repeatable Read** (a transaction reads data, and then another transaction modifies or deletes that data before the first transaction complete.)
            3. Used in applications where exact consistency is less critical and performance is priority.
            4. e.g. TA reads `balance` as 1000 and continues and in between TB updates `balance` to 1002 so in second read TA will read inconsistent value.
         4. **Repeatable Read**
            1. Once a transaction reads data, It is locked until the transaction completes.
            2. This guarantees no dirty reads, No Non Repeatable Reads. **Phantom reads** (occurs when two same queries are executed, but the rows retrieved by the two, are different, bcs new row is added or existing got deleted by some transaction) may occur.
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
   1. Only one master
   2. Slave only serve reads, and master server both read and write.
   3. Master replicates writes to slaves
   4. If master fails then either the DB can operate in read mode or we have to promote any slave to master or provision a new master.
   5. Disadvantages
      1. potential loss if master fails before any newly written data is written to slaves.
      2. Lot of write replays to slave can slow down slave.
4. **Master Master replication**
   1. Two or more servers act as master.
   2. Both masters serve reads and writes and coordinate with each other on writes. If either master goes down, the system can continue to operate with both reads and writes.
   3. Disadvantage
      1. All of master-slave
      2. need some logic to figure out where to write
      3. conflict resolution is time consuming
5. **Federation**
   1. Federation (Functional Partitioning) splits up the database by function. for e.g. instead of a single monolith database you can have three database : forums, users and products, resulting in less read and write traffic to each DB.
   2. Small database with same data can reduce cache miss as well.
   3. Disadvantage
      1. Not effective if our schema requires huge functions or tables
      2. Need to update application logic to determine which database to read and write.
      3. Joining data from two database is complex.
6. **Sharding** [ref1](../gaurav-sen/Part1.md#7-db-sharding-working) [ref2](./Scalability-files/Database-Scaling.md)
7. **SQL Tuning**
   1. SQL tuning is the process of optimizing SQL queries and the database schema to improve the performance of a system.
   2. Some common ways
      1. Avoid expensive joins
      2. Use Good indices
      3. Partitioning, sharding in better way
8. **Denormalization**
   1. It helps to improve read performance at the cost of some write performance. Here some copies of data are duplicates across tables to avoid expensive joins.
   2. In most system reads are used more than write to increasing read performance is good.

## Non-Relational Database (NoSql)

1. Here Data is denormalized, and joins are generally done in the application code.
2. Most NoSql Databases lacks true ACID transactions and favours eventual consistency.
3. For these BASE is used to describe properties in comparison to [CAP theorem](./Availability-Consistency.md#cap-theorem).
   1. Basically available
   2. Soft state
   3. Eventual Consistency
4. Types
   1. **Key value stores**
      1. It allows for O(1) reads.
      2. It allows to store metadata with a value.
      3. It commonly used in `Session storage` , `Caching` and `Real time data processing`.
      4. e.g. Redis, DynamoDB.
   2. **Document Store**
      1. A document store is centered around documents (XML, JSON, binary, etc), where a document stores all information for a given object.
      2. It provides APIs or a query language to query based on the internal structure of documents.
      3. Documents are organized by collections, tags, metadata or directories.
      4. Useful where data model evolves over time.
      5. It is commonly used in `CMS` , `E-commerce`.
      6. e.g. MongoDB, CouchDB.
   3. **Graph Database**
      1. They represent data as graphs, consisting of nodes (entities), edges (relationships between entities), and properties (information associated with nodes and edges).
      2. Commonly useful in `Social Network`, `Knowledge Graphs`, `Recommendation Systems`.
      3. e.g. Neo4j, Amazon Neptune.
   4. **Wide Column**
      1. A wide-column database is a type of NoSQL database in which the names and format of the columns can vary across rows, even within the same table. Wide-column databases are also known as column family databases.
      2. A wide column store's basic unit of data is a column (name/value pair). A column can be grouped in column families (analogous to a SQL table). Super column families further group column families. You can access each column independently with a row key, and columns with the same row key form a row. Each value contains a timestamp for versioning and for conflict resolution.
      3. They organize data into tables with a flexible and dynamic column structure. They are designed to handle large-scale, distributed data storage and provide high scalability and performance.
      4. e.g. HBase, Google Bigtable, Apache Cassandra
      5. ![Image](/img/hld/wide-column-db-1.png) ![Image](/img/hld/wide-column-db-2.png)

## SQL vs NoSql

![Image](/img/hld/SQl-vs-Nosql.png)
