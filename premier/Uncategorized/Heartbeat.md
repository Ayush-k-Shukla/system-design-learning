# Heartbeat

1. In distributed system, it is a periodic message sent from one component to monitor each other health status

### Why Needed

1. Monitoring
2. Detecting failures
3. Load balancing
4. Triggering recovery actions

### Working

1. The sender(node) sends message to receiver(monitor) at a regural interval.
2. if receiver don't receive message after Timeout specifiedś it mark node as failed/unavailable.
3. Then system can take appropriate actions like, re-route traffic, alert...
4. **There are some nuances**
   1. **Frequency**
      1. should be optimal, not too less not much
   2. **Timeout**
      1. it depends on app need
   3. **Payload**
      1. generally it contain small info, but it can also contain current load, health metrics, version etc.

### Types

1. Push
2. Pull

### Challenges

1. Network congestion
2. Resource usage
3. False positive - Poorly configured heartbeat intervals might lead to false positives in failure detection, where a slow but functioning component is incorrectly identified as a failed one.

### Real world examples

1. **Database Replication:** Primary and replica databases often exchange heartbeats to ensure data is synchronized and to trigger failover if the primary becomes unresponsive.

2. **Kubernetes:** In the Kubernetes container orchestration platform, each node sends regular heartbeats to the control plane to indicate its availability. The control plane uses these heartbeats to track the health of nodes and make scheduling decisions accordingly.

3. **Elasticsearch:** In an Elasticsearch cluster, nodes exchange heartbeats to form a gossip network. This network enables nodes to discover each other, share cluster state information, and detect node failures.
