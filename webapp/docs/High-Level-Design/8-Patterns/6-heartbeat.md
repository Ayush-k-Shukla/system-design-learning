# 💓 Heartbeat

- In a distributed system, it is a periodic message sent from one component to monitor each other's health status.

### Why Needed

- Monitoring
- Detecting failures
- Load balancing
- Triggering recovery actions

### Working

- The sender (node) sends a message to the receiver (monitor) at a regular interval.
- If the receiver doesn't receive a message after the specified timeout, it marks the node as failed/unavailable.
- Then the system can take appropriate actions like re-routing traffic or sending an alert.
- **There are some nuances:**
  - **Frequency**
    - Should be optimal, not too little, not too much
  - **Timeout**
    - It depends on the application's needs
  - **Payload**
    - Generally, it contains small info, but it can also contain current load, health metrics, version, etc.

### Types

- Push
- Pull

### Challenges

- Network congestion
- Resource usage
- False positives - Poorly configured heartbeat intervals might lead to false positives in failure detection, where a slow but functioning component is incorrectly identified as a failed one.

### Real World Examples

- **Database Replication:** Primary and replica databases often exchange heartbeats to ensure data is synchronized and to trigger failover if the primary becomes unresponsive.
- **Kubernetes:** In the Kubernetes container orchestration platform, each node sends regular heartbeats to the control plane to indicate its availability/ The control plane uses these heartbeats to track the health of nodes and make scheduling decisions accordingly.
- **Elasticsearch:** In an Elasticsearch cluster, nodes exchange heartbeats to form a gossip network. This network enables nodes to discover each other, share cluster state information, and detect node failures.
