## **Databases**

##### Relational Database

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
   4. **Durability**
3. Master slave replication
4. Master Master replication
5. Federation
6. Sharding
7. SQL Tuning
8. Denormalization
