# Availability and Consistency

## Availability

```jsx
Availability = Uptime / (Uptime + Downtime);
```

1. Uptime -The period during which system is functional and accessible
2. Downtime - The period during which a system is unavailable due to failures, maintenance or other issues
3. Availability is generally referred in terms of “nine”

   ![https://substackcdn.com/image/fetch/f_auto,q_auto:good,fl_progressive:steep/https%3A%2F%2Fsubstack-post-media.s3.amazonaws.com%2Fpublic%2Fimages%2Fe271d918-4a3b-4c74-ada6-6ce1b01a30ef_1632x912.png](https://substackcdn.com/image/fetch/f_auto,q_auto:good,fl_progressive:steep/https%3A%2F%2Fsubstack-post-media.s3.amazonaws.com%2Fpublic%2Fimages%2Fe271d918-4a3b-4c74-ada6-6ce1b01a30ef_1632x912.png)

4. **Parallel Availability**

   1. in parallel setup multiple component (or systems) operate independently. The systems overall availability is higher because it depends on the availability of at least one functioning component.
   2. If one component fails, other components can still handle the request.

      ```jsx
      Availability (Total) = 1 - (1 - Availability (Foo)) * (1 - Availability (Bar))
      ```

5. **Sequential Availability**

   1. In a sequence setup, multiple components depend on each other. The overall system is only available if all components in the sequence are operational.
   2. A failure in any single component causes the entire system to fail.

      ```jsx
      Availability (Total) = Availability (Foo) * Availability (Bar)
      ```

6. **Strategies for improving availability**

   1. Redundancy
   2. Load balancing
   3. Failover mechanism
      1. It is the process of automatically switching to a backup system, component or resource when a primary system fails.
      2. **Types**
         1. **Active-Passive Failover**
            1. The backup (passive) system is kept in a standby mode and becomes active only when the primary system fails.
            2. e.g. A secondary database or server that takes over when the primary one crashes.
         2. **Active-Active Failover**
            1. Multiple systems (or nodes) are active and share the load simultaneously. If one fails, others continue handling the traffic.
            2. e.g. Load-balanced web servers where all servers are active and share requests.
   4. Data replication
   5. Monitoring and Alerts

## CAP Theorem

1. CAP stands for **Consistency**, **Availability**, and **Partition Tolerance**, and the theorem states that:

> **It is impossible for a distributed data store to simultaneously provide all three guarantees.**

1. **Consistency (C)**

   1. every read receives the most recent write or an error
   2. This means that all working nodes in a distributed system will return the same data at any given time.
   3. It is crucial for applications where having most up to date data is critical such as financial systems.

2. **Availability (A)**

   1. Every request (read or write) receives a non-error response, without the guarantee that it contains the most recent write.
   2. This means the system is always operational and responsive even if response from some node don’t represent most up to date data.
   3. It is important for system which need to remain operational all the time such as online retail system.

3. **Partition Tolerance (P)**

   1. The system continues to operate despite an arbitrary number of messages being dropped (or delayed) by the network between nodes.
   2. Partition Tolerance means that the **system continues to function despite network partitions** where nodes cannot communicate with each other.
   3. Partition Tolerance is essential for distributed systems because network failures can and do happen. A system that tolerates partitions can maintain operations across different network segments.

4. **Trade-Off**

   1. **CP**
      1. These system prioritize consistency and can tolerate network partitions at cost of availability. During partition system may reject some requests to maintain consistency.
      2. Traditional relational databases, such as MySQL and PostgreSQL, when configured for strong consistency, prioritize consistency over availability during network partitions.
      3. Banking systems prefers consistency over availability
   2. **AP**
      1. These systems ensure availability and can tolerate network partitions, but at the cost of consistency. During a partition, different nodes may return different values for the same data.
      2. like Cassandra and DynamoDB
      3. Amazon’s Shopping cart designed to accept items always
   3. **CA**
      1. In the absence of partitions, a system can be both consistent and available. However, network partitions are inevitable in distributed systems, making this combination impractical.
      2. Like single node DB, this is theoretically impossible
   4. **In real world we use more flexible approaches like**
      1. **Eventual consistency**
         1. For many systems, strict consistency isn't always necessary.
         2. Eventual consistency can provide a good balance where updates are propagated to all nodes eventually, but not immediately.
         3. like DNS and CDN
      2. **Strong Consistency**
         1. A model ensuring that once a write is confirmed, any subsequent reads will return that value.
      3. **Tunable Consistency**
         1. It is allows developers to configure consistency based on specific needs.
         2. Provide balance between strong and eventual consistency
         3. Systems like Cassandra allow configuring the level of consistency on a per-query basis, providing flexibility.
         4. Applications needing different consistency levels for different operations, such as e-commerce platforms where order processing requires strong consistency but product recommendations can tolerate eventual consistency.

## **PACELC theorem**

1. If a partition occurs (P), a system must choose between Availability (A) and Consistency (C). Else (E), when there is no partition, the system must choose between Latency (L) and Consistency (C).
