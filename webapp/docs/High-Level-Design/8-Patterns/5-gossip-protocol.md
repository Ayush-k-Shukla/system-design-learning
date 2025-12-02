# 🗣️ Gossip Protocol

1. Some problems that we face in a distributed system are:
   1. Maintaining the system state (liveness of nodes)
   2. Communication between nodes
2. Potential solutions to these problems:
   1. Centralized state management service
   2. Peer-to-peer (p2p) state management service

## Centralized State Management Service

1. We can use something like Apache Zookeeper as a [service discovery](../1-Introduction/2-things-to-know-when-building-microservice.md#-service-discovery) to keep track of the state of every node in the system.
2. It is good for CP in the CAP theorem but it introduces a single point of failure (SPOF) and has scalability issues.

## Peer-to-Peer State Management Service

1. It is inclined toward AP in the CAP theorem and gives eventual consistency.
2. The gossip protocol algorithms can be used to implement p2p state management service with high scalability and improved resilience.
3. The **gossip protocol** is also known as the **epidemic protocol** because the transmission of messages is similar to the way epidemics spread.

## Broadcast Protocols

### Point-to-Point Broadcast

Producer-consumer model

### Eager Reliable Broadcast

Every node re-broadcasts the messages to every other node via reliable network links. This approach provides improved fault tolerance because messages are not lost when both the producer and the consumer fail simultaneously. The message will be re-broadcast by the remaining nodes. The caveats of eager reliable broadcast are the following:

1. Significant network bandwidth usage due to O(n²) messages being broadcast for n nodes
2. The sending node can become a bottleneck due to O(n) linear broadcast
3. Every node stores the list of all the nodes in the system, causing increased storage costs

### Gossip Protocol

<p align="center">
    <img src="/img/hld/gossip.gif" />
</p>

1. The gossip protocol is a decentralized peer-to-peer communication technique to transmit messages in a large distributed system.
2. The key concept of the gossip protocol is that every node periodically sends out a message to a subset of other random nodes. The entire system will receive the particular message eventually with a high probability.
3. The gossip protocol is a technique for nodes to build a global map through limited local interactions.
4. The gossip protocol is built on a robust, scalable, and eventually consistent algorithm. It is reliable because if one node fails, the message will be delivered by another node.
5. Why gossip is an optimal choice in large distributed systems:
   1. Limits the number of messages transmitted by each node
   2. Tolerates network and node failures

## Working of Gossip Protocols

1. **Node Selection:** Each node periodically selects a few random nodes from the network.
2. **Information Exchange:** The selected nodes share updates or data (e.g., membership lists, system status, or events).
3. **Propagation:** The receiving nodes then relay the received information to another set of random nodes in subsequent rounds.
4. **Convergence:** After several rounds, all nodes in the network will have received the update, ensuring eventual consistency.

## Types of Gossip Protocols

### Anti-entropy (Full Sync)

Nodes randomly select peers to exchange their entire datasets, correcting any discrepancies. It takes extra bandwidth as the whole dataset is sent.

### Rumor Mongering (probablistic propagation)

Nodes spread new information to random peers, who then decide probabilistically whether to continue spreading it.

### Epidemic-style membership

Nodes share membership information to maintain an updated list of active nodes, facilitating dynamic network topology.

### Agreegation based

Nodes exchange partial data to compute global aggregates like averages or sums.

## Strategies to Spread a Message through Gossip Protocol

### Push based

The sender selects random peers and sends the message to them. Those peers then continue spreading the message to other random peers.

1. **Pros**
   1. Simple to implement
   2. works well for rapidly spreading new updates
2. **Cons**
   1. can lead to redundant message sent
   2. might take longer to reach all nodes in large networks

### Pull based

Nodes periodically contact random peers and ask if they have any new messages. If a peer has a new message, it shares it.

1. **Pros**
   1. More efficient for rare updates (less unnecessary message flooding).
   2. Reduces redundant messages.
2. **Cons**
   1. Updates may take longer to propagate compared to push gossip.

### Push-Pull based (Hybrid)

Nodes both push updates to random peers and pull updates from them.

1. **Pros**
   1. Faster convergence since updates spread in both directions.
   2. Efficient in detecting missing information.
2. **Cons**
   1. Can generate slightly more network traffic than pure pull gossip.

## Applications

1. **Distriuted Databases** - ensure eventual consistency and data replication
2. **Blockchain and Cryptocurrencies** - Propagates transactions and blocks efficiently across decentralized networks.
3. leader election

## Disadvantages

1. Eventual consistency
2. Bandwith consumption
3. Unawareness of network patition
4. difficulty in debugging and testing

## Mathematical Assurance of Eventual Delivery

1. In a network of `N` nodes, if each node gossips to `k` (called **fanout**)random peers per round, the number of informed nodes grows exponentially.
2. After `log(N) base k` rounds, nearly all nodes will have received the message.
3. Example: In a network of 1,000 nodes, even if each node gossips to only 3 nodes per round, most nodes will be informed in ~10 rounds.
