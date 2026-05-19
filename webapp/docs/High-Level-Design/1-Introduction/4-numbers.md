# Numbers To Know

## System Scaling Thresholds Matrix

| Component                                                | Key Performance Metrics                                                                               | Scale Triggers (When to Action)                                                                                                 | Core Design Alternatives                                                                                                          |
| :------------------------------------------------------- | :---------------------------------------------------------------------------------------------------- | :------------------------------------------------------------------------------------------------------------------------------ | :-------------------------------------------------------------------------------------------------------------------------------- |
| **Caching**_(e.g., Redis, Memcached)_                    | • ~1 ms latency<br/>• 100k+ operations/sec<br/>• Memory-bound (up to 1TB)                             | • Hit rate < 80%<br/>• Latency > 1ms<br/>• Memory usage > 80%<br/>• High cache churn/thrashing                                  | • Cluster sharding<br/>• Eviction policy adjustments (LRU/LFU)<br/>• Cache-aside vs Write-through changes                         |
| **Databases**<br/>_(e.g., Postgres, MySQL, NoSQL)_       | • Up to 50k transactions/sec (TPS)<br/>• Sub-5ms read latency (cached)<br/>• 64 TiB+ storage capacity | • Write throughput > 10k TPS<br/>• Read latency > 5ms uncached<br/>• Disk capacity > 70-80%<br/>• Geo-distribution requirements | • Read replicas<br/>• Database sharding (Horizontal scaling)<br/>• Secondary indexing / NoSQL migration                           |
| **App Servers**<br/>_(e.g., Go, Java, Node.js)_          | • 100k+ concurrent connections<br/>• 8-64 cores @ 2-4 GHz<br/>• 64-512GB RAM standard (up to 2TB)     | • CPU utilization > 70%<br/>• Response latency > SLA limit<br/>• Active connections near 100k/instance<br/>• Memory usage > 80% | • Horizontal auto-scaling ( stateless )<br/>• Load balancing optimization (Least Conn)<br/>• Async I/O / Event-loop optimizations |
| **Message Queues**<br/>_(e.g., Kafka, Pulsar, RabbitMQ)_ | • Up to 1M msgs/sec per broker<br/>• Sub-5ms end-to-end latency<br/>• Up to 50TB storage              | • Throughput near 800k msgs/sec<br/>• Partition count ~200k per cluster<br/>• Sustained/growing consumer lag                    | • Add partitions/brokers<br/>• Consumer group scaling<br/>• Retention policy tuning                                               |

---

## Core Numbers (Back-of-the-Envelope Blueprint)

### Latency Numbers You Should Know (Jeff Dean's Latency Numbers)

- **L1 cache reference:** 0.5 ns
- **Branch mispredict:** 5 ns
- **L2 cache reference:** 7 ns
- **Mutex lock/unlock:** 25 ns
- **Main memory reference (RAM):** 100 ns
- **Compress 1K bytes with Zippy:** 3,000 ns (3 µs)
- **Send 1K bytes over 1 Gbps network:** 10,000 ns (10 µs)
- **Read 4K randomly from SSD:** 150,000 ns (150 µs)
- **Read 1 MB sequentially from memory:** 250,000 ns (250 µs)
- **Round trip within same datacenter:** 500,000 ns (0.5 ms)
- **Read 1 MB sequentially from SSD:** 1,000,000 ns (1 ms)
- **Disk seek (HDD):** 10,000,000 ns (10 ms)
- **Read 1 MB sequentially from HDD:** 20,000,000 ns (20 ms)
- **Send packet OH to CA to OH (WAN):** 150,000,000 ns (150 ms)

#### Notes

```
1 ns = 10^-9 seconds
1 us = 10^-6 seconds = 1,000 ns
1 ms = 10^-3 seconds = 1,000 us = 1,000,000 ns
```

### Data Multipliers & Storage Quick-Math

Always convert sizes to bytes to make multiplication easy.

- **Byte (B):** 1 byte (typically 1 character)
- **Kilobyte (KB):** `2^10` bytes approx 1,000 bytes
- **Megabyte (MB):** `2^20` bytes approx 1,000,000 bytes (`10^6`)
- **Gigabyte (GB):** `2^30` bytes approx 1,000,000,000 bytes (`10^9`)
- **Terabyte (TB):** `2^40` bytes approx 1,000,000,000,000 bytes (`10^12`)
- **Petabyte (PB):** `2^50` bytes approx 1,000,000,000,000,000 bytes (`10^15`)

### Time & Volume Conversions (The "Rule of 100k")

To quickly convert daily traffic volumes into Queries Per Second (QPS), remember that a day has approximately 86,400 seconds. In an interview, round this up to 100,000 for lightning-fast mental math:

Average QPS = Total Requests per Day / 100,000

- 1 Million requests/day = ~10 to 12 QPS
- 10 Million requests/day = ~100 to 116 QPS
- 100 Million requests/day = ~1,000 to 1,160 QPS
- 1 Billion requests/day = ~10,000 to 11,600 QPS

- Peak QPS: Always multiply your average QPS by 2x to 5x to plan for traffic spikes.

## High-Scale Design Conventions & Constraints

### Availability & SLA Calculations

- **99% ("Two Nines"):** ~3.65 days downtime/year | 14.4 minutes/day
- **99.9% ("Three Nines"):** ~8.76 hours downtime/year | 1.44 minutes/day
- **99.99% ("Four Nines"):** ~52.6 minutes downtime/year | 8.64 seconds/day
- **99.999% ("Five Nines"):** ~5.26 minutes downtime/year | 864 milliseconds/day

### Hardware & Network Baselines

- **Commodity Server Storage:** Modern NVMe drives handle up to **1-3 GB/s sequential reads/writes** and **500k+ IOPS**. HDDs handle around **100-200 MB/s sequential** and only **100-200 IOPS**.
- **Network Bandwidth:** Standard cloud server instances have network interfaces ranging from **10 Gbps to 100 Gbps**. Ensure your data transfer calculations do not saturate the network interface cards (NIC).
- **TCP/IP Socket Limits:** A single IP can theoretically have up to **65,535 ports**. An application server handling incoming traffic is constrained by file descriptors (`ulimit`), memory per connection (roughly 4KB–64KB overhead per TCP connection), and available ports if acting as a reverse proxy or outbound client.

```

```
