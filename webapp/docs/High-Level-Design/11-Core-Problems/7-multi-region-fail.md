# Multi-Region Failover & Disaster Recovery

Multi-region failover is the architectural capability of a distributed system to shift traffic from one geographically isolated deployment (e.g., Mumbai) to another (e.g., Singapore) when the primary region becomes unavailable.

### Why It Exists (Risks of Single-Region)

- **Cloud Outages:** Provider-level failures (AWS, Azure, GCP).
- **Physical Disasters:** Power failures, natural disasters.
- **Human Error:** Deployment bugs, misconfigurations.
- **Compliance:** Data residency and residency laws (e.g., GDPR).
- **Performance:** Latency optimization by serving users from the nearest region.

---

## Types of Failures & Solutions

Failures occur at different layers and require specific mitigation strategies.

| Layer           | Example Failure         | Solution / Mitigation                                         |
| :-------------- | :---------------------- | :------------------------------------------------------------ |
| **Compute**     | K8s cluster crash       | Regional redundancy, automated scaling.                       |
| **Network**     | Region isolation        | Circuit breakers, cross-region routing.                       |
| **Data**        | Corruption / Bad script | Delayed replication, Point-in-Time Recovery (PITR).           |
| **Application** | Poison Pill (bad code)  | Canary deployments, blue-green strategies.                    |
| **Dependency**  | DNS / Storage failure   | Anycast IP, multi-provider DNS, regional storage replication. |

---

## Recovery Metrics (RTO vs. RPO)

These metrics dictate your architectural choice and cost

| Metric                             | Definition                                                                    | Technical Impact                                                                   |
| :--------------------------------- | :---------------------------------------------------------------------------- | :--------------------------------------------------------------------------------- |
| **RTO** (Recovery Time Objective)  | The maximum acceptable delay between failure and service restoration          | Influences traffic steering (DNS vs. Anycast) and server readiness (Warm vs. Cold) |
| **RPO** (Recovery Point Objective) | The maximum amount of data loss (measured in time) tolerated during a failure | Dictates database replication strategy (Sync vs. Async)                            |

---

## Strategy Selection Matrix

| Strategy                                | RTO       | RPO       | Replication                        | Cost    | Use Case               |
| :-------------------------------------- | :-------- | :-------- | :--------------------------------- | :------ | :--------------------- |
| **Backup & Restore**                    | Hours+    | 24 Hours+ | Periodic snapshots                 | Lowest  | Non-critical/Archival  |
| **Pilot Light**                         | Minutes+  | Minutes   | Async Data / Core data backup only | Low     | General Business Apps  |
| **Warm Standby**                        | Minutes   | Seconds+  | Small live footprint               | Medium  | SaaS / E-commerce      |
| **Active-Active (Multi-Leader)**        | Near-Zero | Seconds   | Bi-directional Async               | High    | High-scale Global Apps |
| **Active-Active (Strongly Consistent)** | Zero      | Zero      | Synchronous ACK                    | Highest | Banking / Payments     |

---

## Multi-Region Architectures

### A. Active-Passive (Hot/Warm Standby)

- **Mechanism:** Traffic goes to Region A. Region B is idle or scaled down.
- **Pros:** Simpler consistency, lower operational overhead.
- **Cons:** Non-zero RTO (time to flip the switch), "Cold Cache" problems, wasted infrastructure cost.

### B. Active-Active

- **Mechanism:** Both regions serve traffic simultaneously using Geo-routing or Latency-based routing.
- **Pros:** Near-zero RTO, better latency for global users.
- **Cons:** Extremely complex data consistency, risk of Split-Brain.

---

## The "Split-Brain" Problem

A scenario where the connection between regions fails, but both regions stay alive. Both assume the other is dead and promote themselves to "Leader," leading to data divergence.

### Solutions:

1.  **Quorum (Majority Rule):** Using an odd number of nodes (or a 3rd witness region). A region can only accept writes if it sees >50% of the nodes (e.g., 2 out of 3).
2.  **Fencing (STONITH):** "Shoot The Other Node In The Head." Disabling the power or network access of the isolated region to prevent it from writing data.
3.  **Generation Clock:** Using monotonically increasing "Term" numbers (Raft/Paxos). Old leaders with lower term numbers are rejected.

---

## Database Failover: The Hardest Part

Application servers are stateless and easy to move; databases carry state and are bound by the **CAP Theorem**.

### Replication Models:

- **Asynchronous:** Fast performance, but risk of data loss (**RPO > 0**).
- **Synchronous:** Zero data loss (**RPO = 0**), but high latency because every write must wait for an ACK from the other region.

### Conflict Resolution (Active-Active):

- **Last Write Wins (LWW):** Simple but risky due to clock skew.
- **CRDTs (Conflict-free Replicated Data Types):** Automatic merging (e.g., counters, sets).
- **Vector Clocks:** Tracking causality to see which update happened "after" another.

---

## 6. Traffic Steering & Health Detection

How do you know when to failover?

### Detection:

- **Synthetic Probes:** Pinging `/health` endpoints.
- **Deep Health Checks:** Checking if the DB is writable, not just if the server is "on."

### Steering:

1.  **DNS-Based:** Fast to set up but plagued by **DNS Caching** (TTL issues).
2.  **Global Load Balancing (Anycast):** Best-in-class. Uses a single IP globally; the network routes traffic to the nearest healthy edge.

---

## Operational Best Practices

- **Idempotency:** Ensure retry logic doesn't duplicate transactions (e.g., double-charging).
- **Thundering Herd:** Avoid DB crashes after failover by "warming" the cache in the secondary region or using request coalescing.
- **Chaos Engineering:** Periodically "kill" a region in production (Netflix Chaos Monkey style) to ensure failover works.
- **No Auto-Failback:** Once a region recovers, do not automatically shift traffic back. Stabilize and sync data manually first to avoid "flapping."
