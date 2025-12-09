# 🗣️ Gossip Protocol

- Some problems that we face in a distributed system are:
  - Maintaining the system state (liveness of nodes)
  - Communication between nodes
- Potential solutions to these problems:
  - Centralized state management service
  - Peer-to-peer (p2p) state management service

## Centralized State Management Service

- We can use something like Apache Zookeeper as a [service discovery](../1-Introduction/2-things-to-know-when-building-microservice.md#-service-discovery) to keep track of the state of every node in the system.
- It is good for CP in the CAP theorem but it introduces a single point of failure (SPOF) and has scalability issues.

## Peer-to-Peer State Management Service

- It is inclined toward AP in the CAP theorem and gives eventual consistency.
- The gossip protocol algorithms can be used to implement p2p state management service with high scalability and improved resilience.
- The **gossip protocol** is also known as the **epidemic protocol** because the transmission of messages is similar to the way epidemics spread.

## Broadcast Protocols

### Point-to-Point Broadcast

Producer-consumer model

### Eager Reliable Broadcast

Every node re-broadcasts the messages to every other node via reliable network links. This approach provides improved fault tolerance because messages are not lost when both the producer and the consumer fail simultaneously. The message will be re-broadcast by the remaining nodes. The caveats of eager reliable broadcast are the following:

- Significant network bandwidth usage due to O(n²) messages being broadcast for n nodes
- The sending node can become a bottleneck due to O(n) linear broadcast
- Every node stores the list of all the nodes in the system, causing increased storage costs

### Gossip Protocol

<p align="center">
    <img src="/img/hld/gossip.gif" />
</p>

- The gossip protocol is a decentralized peer-to-peer communication technique to transmit messages in a large distributed system.
- The key concept of the gossip protocol is that every node periodically sends out a message to a subset of other random nodes. The entire system will receive the particular message eventually with a high probability.
- The gossip protocol is a technique for nodes to build a global map through limited local interactions.
- The gossip protocol is built on a robust, scalable, and eventually consistent algorithm. It is reliable because if one node fails, the message will be delivered by another node.
- Why gossip is an optimal choice in large distributed systems:
  - Limits the number of messages transmitted by each node
  - Tolerates network and node failures

## Working of Gossip Protocols

- **Node Selection:** Each node periodically selects a few random nodes from the network.
- **Information Exchange:** The selected nodes share updates or data (e.g., membership lists, system status, or events).
- **Propagation:** The receiving nodes then relay the received information to another set of random nodes in subsequent rounds.
- **Convergence:** After several rounds, all nodes in the network will have received the update, ensuring eventual consistency.

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

- **Pros**
  - Simple to implement
  - works well for rapidly spreading new updates
- **Cons**
  - can lead to redundant message sent
  - might take longer to reach all nodes in large networks

### Pull based

Nodes periodically contact random peers and ask if they have any new messages. If a peer has a new message, it shares it.

- **Pros**
  - More efficient for rare updates (less unnecessary message flooding).
  - Reduces redundant messages.
- **Cons**
  - Updates may take longer to propagate compared to push gossip.

### Push-Pull based (Hybrid)

Nodes both push updates to random peers and pull updates from them.

- **Pros**
  - Faster convergence since updates spread in both directions.
  - Efficient in detecting missing information.
- **Cons**
  - Can generate slightly more network traffic than pure pull gossip.

## Applications

- **Distriuted Databases** - ensure eventual consistency and data replication
- **Blockchain and Cryptocurrencies** - Propagates transactions and blocks efficiently across decentralized networks.
- leader election

## Disadvantages

- Eventual consistency
- Bandwith consumption
- Unawareness of network patition
- difficulty in debugging and testing

## Mathematical Assurance of Eventual Delivery

- In a network of `N` nodes, if each node gossips to `k` (called **fanout**)random peers per round, the number of informed nodes grows exponentially.
- After `log(N) base k` rounds, nearly all nodes will have received the message.
- Example: In a network of 1,000 nodes, even if each node gossips to only 3 nodes per round, most nodes will be informed in ~10 rounds.
