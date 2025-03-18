# Gossip protocol

1. Some problem that we face in a DS are
   1. maintaining the system state(liveness of nodes)
   2. Communication between nodes
2. Potential solution to these problems
   1. Centralized state management service
   2. p2p state management service

## Centralized State management service

1. We can use something like Apache Zookeeper as a [service discovery](../Things-to-Know-when-building-Microservice.md#service-discovery) to keep track of state of every node in the system.
2. It is good for CP in CAP thorem but it introduces SPOF and had scalability issue.

## Peer to Peer State management service

1. It is inclined toward AP in CAP thorem and gives eventual consistency.
2. The gossip protocol algos can be used to implement p2p state management service with high scalability and improved resilience.
3. The **gossip protocol** is also known as the **epidemic protocol** because the tranmission of message is similar to the way how epidemics spread.

## Broadcast Protocols

### Point-to-Point broadcast

Producer consumer model

### Eager reliable broadcast

Every node re-broadcasts the messages to every other node via reliable network links. This approach provides improved fault tolerance because messages are not lost when both the producer and the consumer fail simultaneously. The message will be re-broadcast by the remaining nodes. The caveats of eager reliable broadcast are the following:

1. significant network bandwidth usage due to O(n²) messages being broadcast for n number of nodes
2. sending node can become a bottleneck due to O(n) linear broadcast
3. every node stores the list of all the nodes in the system causing increased storage costs

### Gossip protocol

<p align="center">
    <img src="../images/gossip.gif"/>
</p>

1. The gossip protocol is a decentralized peer-to-peer communication technique to transmit messages in an enormous distributed system.
2. The key concept of gossip protocol is that every node periodically sends out a message to a subset of other random nodes. The entire system will receive the particular message eventually with a high probability.
3. The gossip protocol is a technique for nodes to build a global map through limited local interactions
