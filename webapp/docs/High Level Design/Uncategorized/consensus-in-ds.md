# 🤝 Consensus in Distributed Systems

Consensus in distributed systems is the process by which multiple nodes, connected and collaborating via message passing, agree on a common value to coordinate computations. This is known as **Distributed Consensus**.

In distributed systems, nodes may need to process large computations and must stay updated about each other's results to maintain system-wide consistency.

---

## 🧐 Why Is Consensus Needed? (With Example)

1. **Example:**

   - Suppose we have a banking system and process a transfer of amount `x` from `accountA` to `accountB`. The system must ensure all servers agree on the value of `x`.

2. **Problems Without Consensus:**

   - Server 1 updates `accountA` to `-x` but crashes before updating `accountB` to `+x`.
   - Server 2 sees an inconsistent state.
   - If nodes do not agree on a single correct state, incorrect balances may occur.

3. **How Consensus Solves This:**

   - All nodes first agree on a single order of transactions.
   - Even if some nodes fail, the majority can still ensure correctness.
   - No transaction is lost, and all nodes eventually reach the same state.

4. Consensus is also used in distributed databases to ensure ACID properties.

---

## 🏗️ How to Achieve Consensus in Distributed Systems

> _A non-faulty node is one that is not crashed, attacked, or malfunctioning._

1. All non-faulty nodes should agree on the same value `v`. If even one does not agree, consensus cannot be achieved.
2. The value `v` should be proposed by a non-faulty node.

---

## ⚠️ Challenges

**Most common challenges:**

1. **Crash:**

   - Occurs when a node does not respond to other nodes due to hardware, software, or network faults.
   - Can often be handled by ignoring the node's existence.

2. **Byzantine Failure:**
   - Occurs when one or more nodes behave abnormally (not crashed), forwarding different messages to different peers due to internal or external attacks.
   - A node may act maliciously, sending false information to other nodes (e.g., hacking attempts in blockchain).
   - A consensus algorithm that can handle Byzantine failures can address any type of consensus problem in distributed systems.

---

## 🗳️ Consensus Algorithms

### Voting-Based Algorithms

1. **Practical Byzantine Fault Tolerance (PBFT):**

   - Handles Byzantine failures.
   - Principle: **If more than two-thirds of all nodes are honest, consensus can be reached.**
   - **How it works:**
     1. The client sends a request to the primary node.
     2. The primary node broadcasts the request to all secondary nodes.
     3. All nodes perform the requested service and reply to the client.
     4. The request is considered successful when the client receives similar responses from at least two-thirds of the nodes.
   - **Pros:**
     - Can tolerate up to 1/3 of nodes being faulty.
   - **Cons:**
     - Communication overhead.

2. **Paxos:**

   - One of the most widely used consensus algorithms; a fault-tolerant, majority-based protocol.
   - Used in Google Spanner.
   - **How it works:**
     1. A proposer proposes a value.
     2. Acceptors (majority of nodes) vote on the proposal.
     3. If a majority agree, the value is committed and learned by all nodes.
   - **Pros:**
     - Fault tolerant.
   - **Cons:**
     - Involves multiple message exchanges.

3. **Raft:**
   - Uses leader election.
   - Used in CockroachDB.
   - **How it works:**
     1. A leader is elected among nodes.
     2. The leader receives updates and replicates them to followers.
     3. Once a majority confirm, the update is applied.
   - **Pros:**
     - Simpler than Paxos.
   - **Cons:**
     - If the leader fails, a new election must occur.

### Proof-Based Algorithms

1. **Proof of Work (PoW):**

   - Used in Bitcoin and other blockchains.
   - **How it works:**
     1. A node (miner) solves a complex mathematical puzzle.
     2. The solution (proof) is verified by other nodes.
     3. If valid, the block is added to the blockchain.
   - **Pros:**
     - Highly secure.
   - **Cons:**
     - Slow.
     - Consumes huge amounts of energy.

2. **Proof of Stake (PoS):**
   - Used in Ethereum 2.0.
   - PoS is an alternative to PoW, where validators stake coins instead of solving puzzles.
