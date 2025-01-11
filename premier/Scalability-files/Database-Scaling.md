# Database Scaling

when our app grows the data we need to store also grows so we need to scale our DB as well. there are a few ways to do it

1. **Vertical scaling**
    1. It involves adding extra resource (RAM, CPU, storage)
    2. it also introduces Single point of failure(as all eggs are in same basket)
    3. It is also expensive and can be scaled up to an extent only.(Hardware and other limitations)
2. **Indexing**
    1. Similar to a book index DB index also helps to not scan each row to find the data.
    2. Indexing is mostly applied to the most queried column. over-indexing can increase the write performance.
    3. The index holds the columns along with the pointer to the corresponding rows in the table.
    4. **Working**
        1. Index creation: DBA creates indexes
        2. Index building: DBMS created indexes by scanning table
        3. Query execution: when a query is executed DB engine first sees if an index exist for a queried column
        4. Index search: if an index is found then it searches data using index pointers
        5. Data retrieval: retrieved the data found using indexes
    5. **Types** 
        1. Based on structure and key Attributes
            1. Primary Index 
                1. Automatically created when a primary key constraint is defined. ensure uniqueness and fast lookup using primary key.
            2. Clustered and non clustered(Secondary) index [LINK](https://www.geeksforgeeks.org/difference-between-clustered-and-non-clustered-index/)
        2. Based on Data coverage
            1. Dense index
                1. Has entry in table will have an entry in index.
                2. mostly used in case where we need frequent and fast access.
                3. primary index also like Dense index (as for each value there is an entry )
                4. preferred for small tables
            2. sparse
                1. it contains entry for only some values.
                2. generally has one entry per page/block.
                3. used in large tables and clustered tables
    6. How to Use effectively
        1. index frequently used columns
        2. index selective columns 
            1. index are most effective in column with good spread of data (like index in gender column will be less beneficial than index on id)
3. **Sharding**
    1. a single machine can only store limited data so we need to store data in multiple places.
    2. It is a type of horizontal scaling used to split large DBs into smaller pieces called shards.
    3. **Working**
        1. Sharding Key: it is a unique identifier used to identify which shard data belongs
        2. Data partitioning: It involves splitting data based on the shard key.
    4. **Sharding strategies**
        1. Hash-based
        2. Range-based: data distributed over a range like dates or numbers.
        3. Geo-based
        4. Directory based
    5. Benefits
        1. Performance
        2. Scalability
        3. Geographical distribution
        4. Cost
    6. Complexities
        1. Cross shard joins
        2. Shards have data evenly shared
    7. Best Practices
        1. Use consistent hashing to avoid data movement during shard addition/deletion
        2. Choose the right shard key for even distribution
        3. Handle cross-shard query by data normalization
4. Vertical partitioning
    1. it is useful when our table contains multiple columns(let’s say A, B, C, D) but column A,B are most freq accessed so we can partition A, B into a separate table so no need to query extra columns. 
5. Caching
    1. Store frequently accessed data in a faster storage layer like in-memory.
    2. more abt caching will be covered on the caching page
6. Replication
    1. Maintaining copies of DB across multiple servers.
    2. Preferred for fault-tolerant, ready heavy, Geographically distributed systems.
    3. Types
        1. Synchronous
            1. every write to the primary database is instantly propagated to all replicas. The write operation is only confirmed once all replicas have applied the change.
            2. slower, guarantees data consistency
        2. Asynchronous
            1. Changes to the primary database are replicated to replicas with a slight delay. This offers better performance but with the trade-off of potential data inconsistency between the primary and replicas (known as replication lag).
        3. Semi-Synchronous Replication:
            1. The master waits for at least confirmation from 1 slave
        4. Multi-master
            1. In multi-master replication, multiple databases act as masters, allowing read and write operations on any node. Each change is propagated to other masters to keep them in sync.
            2. (e.g., collaborative applications).
7. Materialized view
    1. Some DB queries are complex and take a long time to execute, so they can slow down performance if called often.
    2. so we can precompute this. thus Materialized views are pre-computed, disk-stored result sets of complex queries.
    3. we also need to define a refresh mechanism for these views.
8. Data Denormalization
    1. Data denormalization is optimizing database performance by intentionally duplicating or aggregating data. 
    2. This is the opposite of data normalization, where data is broken into smaller tables to reduce redundancy and ensure consistency. 
    3. In denormalization, data is consolidated into fewer tables, often sacrificing storage efficiency and some consistency to achieve faster read performance, especially in read-heavy applications.
    4. Example
        1. suppose we have table customer (c_id,c_name,c_add) and orders (o_id,c_id{foreign key},o_date)
        2. in the above case get order details with the customer name and address we have to make a join so to avoid this we can add redundant field c_name and c_add to the orders table.
    5. Usage
        1. Read heavy workload
        2. Real-time analytics