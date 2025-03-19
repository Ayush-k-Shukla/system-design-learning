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
3. The gossip protocol is a technique for nodes to build a global map through limited local interactions.
4. The gossip protocol built on a robust, scalable and eventual consistent algorithm. It is reliable as if one node fail the message will be delivered by another node.
5. Why gossip is a optimal choice in large distributed system
   1. limits the number of messages tranmitted by each node
   2. tolerate network and node failures

## Working of Gossip Protocols

1. **Node Selection**: Each node periodically selects a few random nodes from the network.
2. **Information Exchange:** The selected nodes share updates or data (e.g., membership lists, system status, or events).
3. **Propagation:** The receiving nodes then relay the received information to another set of random nodes in subsequent rounds.
4. **Convergence:** After several rounds, all nodes in the network will have received the update, ensuring eventual consistency.

## Types of Gossip Protocols

### Anti entropy (full sync)

Nodes randomly select peers to exchange thie entire datasets, correcting any discrepancies. It takes extra bandwidth as whole dataset is sent.

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
