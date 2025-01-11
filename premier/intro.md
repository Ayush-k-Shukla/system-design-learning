# Premier

[Scalability](Scalability.md)

[Availability and Consistency](Availability-Consistency.md)

[DNS, CDN, Load balancer, Proxies](DNS-CDN-Load_balancer-Proxies.md)

[Things to Know when building Microservice](Things-to-Know-when-building-Microservice.md)

Pending to read

1. MS architecture in depth
2. DB
   1. RDBMS
   2. NOSQL
3. Caching more
4. Ash

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
3. **Availability vs consistency - see in page [Availability and Consistency](Premier%20122bef10f5218057beefd3ee44e8f84f/Availability%20and%20Consistency%2014bbef10f52180658abffba7131bc721.md)**
