# Consensus in Distributed Syetem

1. In a DS multiple nodes are mutually connected and collaborate with each other through message passing. Now during some computation they need to agree upon a common value to co-ordinate among multiple process. This phenomenon is called **Distributed Consensus**.
2. In a DS it may happen multiple nodes are processing a large computation where they need to know the result of each other to keep them updated about the whole system.

## Why needed with example

1. Suppose we have a banking system and we processed like transfer amount `x` from accountA to accountB. The system must ensure all servers agree on value `x`.
2. **Problems we can have without consensus**
   1. server1 updated accountA to `-x` but crashed before updating accountB to `+x`
   2. server2 sees an inconsistent state.
   3. if nodes not agree on a single correct state, incorrect baance might occur.
3. **How Consensu solve this**
   1. All nodes first agree on a single order of transaction
   2. Even if some node fail the majorty can still ensure correctness
   3. No transaction lost and all nodes eventually reach same state
4. It is also used for Distributed DBs to ensure ACID properties

## How to achieve Consensus in DS

```
* Nonfaulty node means, nodes which is not crashed or attacked or malfunctioning.
```

1. All nonfaulty nodes should agree on a same value `v` if one of them not agree then consensus can not be achieved
2. The value `v` should be proposed by a non-faulty node

## Challenges

Most common

1. **Crash**
   1. It occurs when a node is not responding to other nodes of the system due to some Hardware or Software ot Network fault.
   2. It can be handles easily by ignoring node's existance.
2. **Byzantine failure**
   1. A situation where one or more node is not crashed but behaves abnormally and forward a different message to different peers, due to an internal or external attack on that node. Handling this kind of situation is complicated in the distributed system.
   2. A node may act maliciously, sending false information to other nodes (e.g., hacking attempts in blockchain).
   3. A consensus algorithm, if it can handle Byzantine failure can handle any type of consensus problem in a distributed system.

## Consensus Algorithms

### Voting based

1. **Practical Byzantine Fault Tolerance**
   1. It handles Byzantine failure
   2. It works on principle **If more than two-thirds of all nodes in a system are honest then consensus can be reached.**
   3. **Working**
      1. The client sends a request to the primary node.
      2. The primary nodes broadcast the request to all secondary nodes.
      3. All the nodes perform the service that is requested and send it to the client as a reply.
      4. The request is served successfully when the client received a similar message from at least two-thirds of the total nodes.
   4. **Pros**
      1. Can tolerate up to 1/3 of nodes being faulty.
   5. **Cons**
      1. Communication overhead
2. **Paxos**
   1. Paxos is one of the most widely used consensus algorithms. It is a fault-tolerant, majority-based consensus protocol.
   2. Used in Google Spanner
   3. **Working**
      1. Proposer proposes a value.
      2. Acceptors (Majority of nodes) vote on the proposal.
      3. If a majority of acceptors agree, the value is committed and learned by all nodes.
   4. **Pros**
      1. Fault tolerant
   5. **Cons**
      1. Involves multiple message exchanges
3. **Raft**
   1. It uses leader election
   2. Used in Cockrock DB
   3. **Working**
      1. A leader is elected among nodes.
      2. The leader receives updates and replicates them to followers.
      3. Once a majority of nodes confirm, the update is applied.
   4. **Pros**
      1. Simpler than Paxos
   5. **Cons**
      1. If leader fails a new election must happen

### Proof based

1. **Proof of Work**
   1. Used in bitcoin and blockchain.
   2. **Working**
      1. A node (miner) solves a complex mathematical puzzle
      2. The solution (proof) is verified by other nodes.
      3. If valid, the block is added to the blockchain.
   3. **Pros**
      1. Highly secure
   4. **Cons**
      1. Slow
      2. Consume huge energy
2. **Proof of Stake**
   1. Used in Etherium 2.0
   2. PoS is an alternative to PoW where validators stake coins instead of solving puzzles.
