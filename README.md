Notes for System Design Learning (In Progress)

# Premier

### [Scalability](premier/Scalability.md)

- [Caches](premier/Scalability-files/Caches.md)
- [Clone](premier/Scalability-files/Clone.md)
- [Database Scaling](premier/Scalability-files/Database-Scaling.md)

### [Availability and Consistency](premier/Availability-Consistency.md)

- [Availability](premier/Availability-Consistency.md#availability)
- [CAP Theorem](premier/Availability-Consistency.md#cap-theorem)
- [PACELC Theorem](premier/Availability-Consistency.md#pacelc-theorem)

### [DNS, CDN, Load balancer, Proxies](premier/DNS-CDN-Load_balancer-Proxies.md)

- [Domain Name System](premier/DNS-CDN-Load_balancer-Proxies.md#domain-name-system)
- [CDN](premier/DNS-CDN-Load_balancer-Proxies.md#cdn)
- [Load Balancer](premier/DNS-CDN-Load_balancer-Proxies.md#load-balancer)
- [Proxy Servers](premier/DNS-CDN-Load_balancer-Proxies.md#proxy-servers)
- [Load Balancer vs Reverse Proxy](premier/DNS-CDN-Load_balancer-Proxies.md#load-balancer-vs-reverse-proxy)

### [Databases](premier/Databases.md)

- [Relational Database](premier/Databases.md#relational-database)
- [Non-Relational Database (NoSql)](premier/Databases.md#non-relational-database-nosql)
- [SQL vs NoSql](premier/Databases.md#sql-vs-nosql)

### [Asynchronism](premier/Asynchronism.md)

- [Message Queues](premier/Scalability-files/Message-Queue.md)
- [Task Queues](premier/Scalability-files/Task-Queue.md)
- [Back Pressure](premier/Asynchronism.md#back-pressure)

### [Things to Know when building Microservice](premier/Things-to-Know-when-building-Microservice.md)

- [Service Discovery](premier/Things-to-Know-when-building-Microservice.md#service-discovery)

### [Communication](premier/Communication.md)

- [HTTP](premier/Communication.md#http)
- [Transmission Control Protocol (TCP)](premier/Communication.md#transmission-control-protocol-tcp)
- [User Datagram Protocol (UDP)](premier/Communication.md#user-datagram-protocol-udp)
- [Remote Procedure Call](premier/Communication.md#remote-procedure-call-rpc)
- [Representational State Transfer](premier/Communication.md#representational-state-transfer-rest)
- [gRPC](premier/Communication.md#google-remote-procedure-callgrpc)
- [GraphQL](premier/Communication.md#graphql)
- [Websockets](premier/Communication.md#websockets)
- [Websockets vs Long polling vs Polling](premier/Communication.md#websocket-vs-long-polling-vs-polling)

### [High level Tradeoffs](premier/High-level-tradeoffs.md)

- [Performance vs Scalability](premier/High-level-tradeoffs.md#performance-vs-scalability)
- [Latency vs Throughput](premier/High-level-tradeoffs.md#latency-vs-throughput)
- [Availability vs Consistency](premier/High-level-tradeoffs.md#availability-vs-consistency---see-in-page-availability-and-consistency)
- [Batch vs Stream Processing](premier/High-level-tradeoffs.md#batch-vs-stream-processing)
- [Stateful vs Stateless](premier/High-level-tradeoffs.md#stateful-vs-stateless-design)
- [Concurrency vs Parallelism](premier/High-level-tradeoffs.md#concurrency-vs-parallelism)

### Uncategorized

- [Heartbeat](premier/Uncategorized/Heartbeat.md)
- [Bloom Filters](premier/Uncategorized/Bloom-filters.md)
  - [Traditional vs Scalable vs Counting](/premier/Uncategorized/Bloom-filters.md#traditional-vs-counting-vs-scalable-bloom-filter)
- [Client server Architecture](premier/Uncategorized/client-server-architecture.md)
- [Serverless Architecture](premier/Uncategorized/serverless-arch.md)
- [Cache Control in HTTP](premier/Uncategorized/browser-cache-control.md)
- [Gossip protocol](premier/Uncategorized/gossip-protocol.md)
- [Consensus in Distributed System](premier/Uncategorized/consensus-in-ds.md)

# Technologies

- [Redis sorted set](Technologies/redis-sorted-set.md)

# Questions

- [How to answer](Questions/steps-to-answer-sd.md)
- [TinyUrl](Questions/TinyUrl.md)
- [Leaderboard](Questions/leader-board.md)
- [Pastebin](Questions/pastebin.md)
- [Distributed Unique Id generator based on snowflake](Questions/snowflake.md)
- [CDN](Questions/cdn.md) - In progress

# Implementations

- [Learderboard in Redis](https://github.com/Ayush-k-Shukla/leaderboard-redis)
- [Id Generation using Snowflake](https://github.com/Ayush-k-Shukla/small-dev-projects/tree/main/3.%20snowflake-id)

# Pending

Theory

1. Consensus in DS
2. Distributed Locking
3. Normal locking
4. Idempotency and How can be handled
5. How do DB index work (e.g. Ds used and implemented like B+...)
6. Web app firewall
7. Sse and comp with ws and implementation (support over http1 and http2)
8. little in details of http1 vs 2 vs 3

Questions

1. CDN design
2. UPI

Impl

1. something related to mq
