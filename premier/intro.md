# Premier

Pending to read

1. Communication and different protocols
   1. RPC
   2. REST
   3. gRPC
   4. Graphql

**High level Tradeoffs**

1. **Performance vs Scalability**
   1. If you will have performance issue your system will be slow for a single user
   2. if you will have scalability issue your system will be fast for a user but will be slow under heavy load
   3. **Performance** refers to how efficiently a system executes tasks under a specific workload. It focuses on response time, throughput and resource usage
   4. **Scalability** refers ability of a system to not degrade the performance under increased workload or growing user.
2. **Latency vs Throughput**
   1. **Latency** is the time it takes a single request to be processed or a task to be completed. typically measured in milliseconds or seconds.
   2. **Throughput** refers to the rate at which a system process requests or tasks. typically measured in operations per second (ops), transactions per second.
   3. **Generally we thrive for maximal throughput with acceptable latency**
3. **Availability vs consistency - see in page [Availability and Consistency](Availability-Consistency.md)**
