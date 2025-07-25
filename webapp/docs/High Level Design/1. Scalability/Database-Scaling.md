# 🗄️ Database Scaling

> When your app grows, the data you need to store also grows, so you need to scale your database as well. There are a few ways to do it:

---

## Vertical Scaling

- Involves adding extra resources (RAM, CPU, storage).
- Introduces a single point of failure (all eggs in one basket).
- It is expensive and can only be scaled up to an extent (hardware and other limitations).

## Indexing

- Similar to a book index, a database index helps avoid scanning each row to find the data.
- Indexing is mostly applied to the most queried columns. Over-indexing can decrease write performance.
- The index holds the columns along with pointers to the corresponding rows in the table.

### Working

- **Index creation:** DBA creates indexes.
- **Index building:** DBMS creates indexes by scanning the table.
- **Query execution:** When a query is executed, the DB engine first checks if an index exists for the queried column.
- **Index search:** If an index is found, it searches data using index pointers.
- **Data retrieval:** Retrieves the data found using indexes.

### Types

- **Based on structure and key attributes:**
  - **Primary Index:**
    - Automatically created when a primary key constraint is defined. Ensures uniqueness and fast lookup using the primary key.
  - **Clustered and Non-Clustered (Secondary) Index:** [LINK](https://www.geeksforgeeks.org/difference-between-clustered-and-non-clustered-index/)
    - Clustered index will change the order of rows in the physical table (like primary key will be sorted).
    - Only one clustered index per table.
    - Non-clustered is a normal index.
- **Based on data coverage:**
  - **Dense Index:**
    - Every entry in the table will have an entry in the index.
    - Mostly used where frequent and fast access is needed.
    - Primary index is also like a dense index (as for each value there is an entry).
    - Preferred for small tables.
  - **Sparse Index:**
    - Contains entries for only some values.
    - Generally has one entry per page/block.
    - Used in large tables and clustered tables.

### **How to use effectively:**

- Index frequently used columns.
- Index selective columns.
  - Indexes are most effective on columns with a good spread of data (e.g., index on gender column is less beneficial than index on id).

## Sharding

- A single machine can only store limited data, so we need to store data in multiple places.
- It is a type of horizontal scaling used to split large databases into smaller pieces called shards.

### Working

- **Sharding Key:** A unique identifier used to determine which shard data belongs to.
- **Data partitioning:** Involves splitting data based on the shard key.

### Sharding strategies

- Hash-based
- Range-based: Data distributed over a range like dates or numbers.
- Geo-based
- Directory-based

### Benefits

- Performance
- Scalability
- Geographical distribution
- Cost

### Complexities:

- Cross-shard joins
- Ensuring shards have data evenly shared

### Best Practices:

- Use consistent hashing to avoid data movement during shard addition/deletion
- Choose the right shard key for even distribution
- Handle cross-shard query by data normalization

## Vertical partitioning

- it is useful when our table contains multiple columns(let’s say A, B, C, D) but column A,B are most freq accessed so we can partition A, B into a separate table so no need to query extra columns.

## Caching

- Store frequently accessed data in a faster storage layer like in-memory.
- more abt caching will be covered on the caching page

## Replication

- Maintaining copies of the database across multiple servers.
- Preferred for fault-tolerant, read-heavy, geographically distributed systems.
- **Types:**
  - **Synchronous:**
    - Every write to the primary database is instantly propagated to all replicas. The write operation is only confirmed once all replicas have applied the change.
    - Slower, guarantees data consistency.
  - **Asynchronous:**
    - Changes to the primary database are replicated to replicas with a slight delay. This offers better performance but with the trade-off of potential data inconsistency between the primary and replicas (known as replication lag).
  - **Semi-Synchronous Replication:**
    - The master waits for at least one confirmation from a slave.
  - **Multi-master:**
    - In multi-master replication, multiple databases act as masters, allowing read and write operations on any node. Each change is propagated to other masters to keep them in sync.
    - (e.g., collaborative applications).

## Materialized View

- Some database queries are complex and take a long time to execute, which can slow down performance if called often.
- Materialized views are pre-computed, disk-stored result sets of complex queries.
- You also need to define a refresh mechanism for these views.

## Data Denormalization

- Data denormalization is optimizing database performance by intentionally duplicating or aggregating data.
- This is the opposite of data normalization, where data is broken into smaller tables to reduce redundancy and ensure consistency.
- In denormalization, data is consolidated into fewer tables, often sacrificing storage efficiency and some consistency to achieve faster read performance, especially in read-heavy applications.
  - **Example:**
    - Suppose we have a table `customer` (c_id, c_name, c_add) and `orders` (o_id, c_id [foreign key], o_date).
    - In the above case, to get order details with the customer name and address, we have to make a join. To avoid this, we can add redundant fields c_name and c_add to the orders table.
  - **Usage:**
    - Read-heavy workload
    - Real-time analytics
