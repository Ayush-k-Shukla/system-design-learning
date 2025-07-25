# 💓 Heartbeat

1. In a distributed system, it is a periodic message sent from one component to monitor each other's health status.

### Why Needed

1. Monitoring
2. Detecting failures
3. Load balancing
4. Triggering recovery actions

### Working

1. The sender (node) sends a message to the receiver (monitor) at a regular interval.
2. If the receiver doesn't receive a message after the specified timeout, it marks the node as failed/unavailable.
3. Then the system can take appropriate actions like re-routing traffic or sending an alert.
4. **There are some nuances:**
   1. **Frequency**
      1. Should be optimal, not too little, not too much
   2. **Timeout**
      1. It depends on the application's needs
   3. **Payload**
      1. Generally, it contains small info, but it can also contain current load, health metrics, version, etc.

### Types

1. Push
2. Pull

### Challenges

1. Network congestion
2. Resource usage
3. False positives - Poorly configured heartbeat intervals might lead to false positives in failure detection, where a slow but functioning component is incorrectly identified as a failed one.

### Real World Examples

1. **Database Replication:** Primary and replica databases often exchange heartbeats to ensure data is synchronized and to trigger failover if the primary becomes unresponsive.
2. **Kubernetes:** In the Kubernetes container orchestration platform, each node sends regular heartbeats to the control plane to indicate its availability. The control plane uses these heartbeats to track the health of nodes and make scheduling decisions accordingly.
3. **Elasticsearch:** In an Elasticsearch cluster, nodes exchange heartbeats to form a gossip network. This network enables nodes to discover each other, share cluster state information, and detect node failures.
