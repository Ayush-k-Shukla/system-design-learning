Notes for System Design Learning (In Progress)

---

## Table of Contents

- [HLD (High Level Design)](#hld-high-level-design)
  - [Scalability](#scalability)
  - [Availability and Consistency](#availability-and-consistency)
  - [DNS, CDN, Load balancer, Proxies](#dns-cdn-load-balancer-proxies)
  - [Databases](#databases)
  - [Asynchronism](#asynchronism)
  - [Things to Know when building Microservice](#things-to-know-when-building-microservice)
  - [Communication](#communication)
  - [High level Tradeoffs](#high-level-tradeoffs)
  - [Uncategorized](#uncategorized)
  - [Types of Attacks](#types-of-attacks)
- [LLD (Low Level Design)](#lld-low-level-design)
- [Technologies](#technologies)
- [Questions (HLD)](#questions-hld)
- [Implementations](#implementations)
- [LLD Implementations](#lld-implementations)
- [Pending](#pending)
- [Other References](#other-references)

---

# HLD (High Level Design)

> Concepts and patterns for designing scalable, reliable, and maintainable systems.

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
- [HTTP1.1 vs HTTP2 vs HTTP3](premier/Communication.md#http11-vs-http2-vs-http3)
- [Transmission Control Protocol (TCP)](premier/Communication.md#transmission-control-protocol-tcp)
- [User Datagram Protocol (UDP)](premier/Communication.md#user-datagram-protocol-udp)
- [Remote Procedure Call](premier/Communication.md#remote-procedure-call-rpc)
- [Representational State Transfer](premier/Communication.md#representational-state-transfer-rest)
- [gRPC](premier/Communication.md#google-remote-procedure-callgrpc)
- [GraphQL](premier/Communication.md#graphql)
- [Websockets](premier/Communication.md#websockets)
- [Server sent Events (SSE)](premier/Communication.md#server-sent-events-sse)
- [Websockets vs SSE vs Long polling vs Polling](premier/Communication.md#websocket-vs-sse-vs-long-polling-vs-polling)

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
- [Idempotency in Distributed System](premier/Uncategorized/Idempotency.md)
- [Normal and Distributed Locks](premier/Uncategorized/distributed-locks.md)
  - [Normal locks](premier/Uncategorized/distributed-locks.md#normal-locking-single-node-locking)
  - [Distributed locks](premier/Uncategorized/distributed-locks.md#distributed-locking-multi-node-locking)
- [Consistent Hashing](premier/Uncategorized/consistent-hashing.md)

### [Types of Attacks](premier/attacks.md)

- [Denial of Service (DoS)](premier/attacks.md#denial-of-a-service-dos)
- [Distributed Denial of Service (DoS)](premier/attacks.md#distributed-denial-of-a-service-ddos)
- [Man In Middle](premier/attacks.md#main-in-the-middle-mitm)
- [Dns Spoofing](premier/attacks.md#dns-spoofing)
- [Sql Injection](premier/attacks.md#sql-injection)
- [Cross Site Scripting(XSS)](premier/attacks.md#cross-site-scripting-xss)
- [Cross Site Request Forgery(CSRF)](premier/attacks.md#cross-site-request-forgery-csrf)
- [Remote Code Execution(RCE)](premier/attacks.md#remote-code-execution)
- [Path Traversal Attacks](premier/attacks.md#path-traversal-attack)
- [Brute force attacks](premier/attacks.md#brute-force-attack)
- [Session Hijacking](premier/attacks.md#session-hijacking)
- [OAuth and JWT Attacks](premier/attacks.md#oauth-and-jwt-attacks)

---

# LLD (Low Level Design)

> Object-oriented and design pattern concepts for building robust software components.

- [OOPS](lld/oop.md)
- [SOLID](lld/solid.md)
- [DRY](lld/solid.md#dry-dont-repeat-yourself)
- [KISS](lld/solid.md#kiss-keep-it-simple-stupid)
- [YAGNI](lld/solid.md#yagni-you-arent-gonna-need-it)
- [Singleton](lld/singleton.md)
- [Factory](lld/factory.md)
- [Abstract Factory](lld/abs-factory.md)

---

# Technologies

> Key technologies and data structures used in system design.

- [Redis sorted set](Technologies/redis-sorted-set.md)

---

# Questions (HLD)

> Common system design questions and their solutions.

- [How to answer](Questions/steps-to-answer-sd.md)
- [TinyUrl](Questions/TinyUrl.md)
- [Leaderboard](Questions/leader-board.md)
- [Pastebin](Questions/pastebin.md)
- [Distributed Unique Id generator based on snowflake](Questions/snowflake.md)
- [CDN](Questions/cdn.md) - In progress

---

# Implementations

> Reference implementations and code repositories.

- [Leaderboard in Redis](https://github.com/Ayush-k-Shukla/leaderboard-redis)
- [Id Generation using Snowflake](https://github.com/Ayush-k-Shukla/small-dev-projects/tree/main/3.%20snowflake-id)
- [Server Sent Events](https://github.com/Ayush-k-Shukla/small-dev-projects/tree/main/5.%20server-sent-events)
- [Consistent Hashing](https://github.com/Ayush-k-Shukla/small-dev-projects/tree/main/6.%20consistent-hashing)

---

# LLD Implementations

> Basic implementations of some LLD questions.

- [Logger](/lldImplementation/logger/readme.md)
- [Parking lot](/lldImplementation/parkinglot/readme.md)

---

# Pending

> 🚧 Topics and implementations to be covered or improved.

**Theory**

1. How do DB indexes work (e.g. DS used and implemented like B+...)
2. Web app firewall
3. Learn more about OSI model and what works in which layer
4. Back of envelope estimation in SD
5. What happens when we type in URL browser

**Questions**

1. CDN design
2. UPI

**Impl**

1. Something related to MQ

**LLD**

1. Design patterns
   1. Builder
   2. Prototype

---

# Other References

- [Consistent Hashing (Gaurav Sen)](gaurav-sen/Part1.md#consistent-hashing-link)
