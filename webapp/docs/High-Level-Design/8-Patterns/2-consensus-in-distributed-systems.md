# Consensus in Distributed Systems

- Consensus in distributed systems is the process by which multiple nodes, connected and collaborating via message passing, agree on a single value, decision, or ordered sequence of operations despite failures. This is known as **Distributed Consensus**.
- In distributed systems, nodes may need to process large computations and must stay updated about each other's results to maintain system-wide consistency.

---

# Why Is Consensus Needed? (With Example)

- **Example:**
  - Suppose we have a banking system and process a transfer of amount `x` from `accountA` to `accountB`.
  - The system must ensure all servers agree on the value and order of the transaction.

- **Problems Without Consensus:**
  - Server 1 updates `accountA` to `-x` but crashes before updating `accountB` to `+x`.
  - Server 2 sees an inconsistent state.
  - If nodes do not agree on a single correct state, incorrect balances may occur.

- **How Consensus Solves This:**
  - All nodes first agree on a single order of transactions.
  - Even if some nodes fail, the majority can still ensure correctness.
  - No transaction is lost, and all nodes eventually reach the same state.

- Consensus is heavily used in distributed databases to maintain replication consistency, transaction ordering, and strong consistency guarantees across replicas.

---

# How to Achieve Consensus in Distributed Systems

> _A non-faulty node is one that is not crashed, attacked, or malfunctioning._

- All non-faulty nodes should eventually agree on the same value `v`. If even one does not agree, consensus cannot be achieved.
- The value `v` should be proposed by a valid non-faulty node.

---

# Core Properties of Consensus

### 1. Agreement

- All non-faulty nodes must agree on the same value.

### 2. Validity

- The agreed value must have been proposed by a valid node.

### 3. Termination (Liveness)

- All non-faulty nodes eventually reach a decision.

### 4. Integrity

- A value is decided at most once.

---

# Quorum / Majority

- Most consensus algorithms rely on quorum-based agreement.
- A quorum is the minimum number of nodes required to safely make progress.
- Consensus usually requires a majority of nodes to agree.

## Formula

```txt
Quorum = floor(N / 2) + 1
```

## Examples

```txt
3 nodes -> quorum = 2
5 nodes -> quorum = 3
7 nodes -> quorum = 4
```

- Using quorums helps prevent conflicting decisions and split-brain scenarios.

---

# Common Challenges

## Crash Failure

- Occurs when a node does not respond to other nodes due to hardware, software, or network faults.
- Crash failures are typically handled using:
  - Replication
  - Timeouts
  - Retries
  - Quorum-based mechanisms

## Byzantine Failure

- Occurs when one or more nodes behave abnormally (not crashed), forwarding different messages to different peers due to internal or external attacks.
- A node may act maliciously, sending false information to other nodes.
- Common in blockchain and highly adversarial systems.

### Example

```txt
Node A tells Node B: value = 10
Node A tells Node C: value = 50
```

- Algorithms tolerant to Byzantine failures can also tolerate simpler crash failures, but they are significantly more complex and expensive.

---

# FLP Impossibility Theorem

- The FLP theorem states that in a fully asynchronous distributed system, deterministic consensus cannot be guaranteed if even one node may fail.

- Because of this limitation, practical systems rely on:
  - Timeouts
  - Failure detectors
  - Randomized elections
  - Partial synchrony assumptions

---

# Split Brain Problem

- Split brain occurs when multiple nodes believe they are the leader simultaneously.
- This usually happens because of network partitions.

## Example

```txt
Partition 1 -> Node A thinks it is leader
Partition 2 -> Node B thinks it is leader
```

- This can cause:
  - Conflicting writes
  - Data inconsistency
  - Corruption

- Consensus algorithms prevent split brain using:
  - Majority quorum rules
  - Terms/Epochs
  - Leader leases

---

# Consensus Algorithms

## Voting-Based Algorithms

### 1. Practical Byzantine Fault Tolerance (PBFT)

- Handles Byzantine failures.
- Principle:
  - If more than two-thirds of all nodes are honest, consensus can be reached.

## How it works

- The client sends a request to the primary node.
- The primary node broadcasts the request to all secondary nodes.
- All nodes execute the request independently.
- Each node replies directly to the client.
  - Replies are sent directly because the primary node itself may be malicious.
- The request is considered successful when the client receives matching responses from at least two-thirds of the nodes.

## Pros

- Can tolerate up to `1/3` faulty or malicious nodes.
- Strong consistency.

## Cons

- High communication overhead.
- Difficult to scale to large clusters.

---

### 2. Paxos

- One of the most widely used consensus algorithms.
- A fault-tolerant, majority-based protocol.
- Used in systems like Google Spanner and Chubby.

## How it works

- Paxos works using multiple phases:
  - Propose
  - Promise
  - Accept
- A proposer proposes a value.
- Acceptors vote on the proposal.
- If a majority accept the proposal, the value is committed and learned by all nodes.
- Paxos reaches consensus through majority agreement and multiple rounds of message exchanges.

## Pros

- Fault tolerant.
- Proven correctness.

## Cons

- Difficult to understand and implement.
- Multiple message exchanges increase complexity.

---

### 3. Raft

- Raft is a consensus algorithm designed to be easier to understand than Paxos.
- Uses leader election as a core mechanism.
- Used in systems like CockroachDB, etcd, and Consul.

## How it works

- Nodes hold periodic elections to choose a leader.
- The elected leader receives client requests.
- Raft achieves consensus by replicating an ordered log (sequence of operations) across follower nodes.
  - The goal is Every healthy node should eventually have the exact same log in the exact same order.
- Followers acknowledge replicated log entries.
- Once a majority confirms the entry, the update is committed and applied.
- If the leader fails or becomes unreachable, a new election automatically begins.

## Pros

- Easier to understand than Paxos.
- Clear leader-based architecture.
- Simplifies log replication and consensus.

## Cons

- Availability depends on timely leader election.
- Elections temporarily pause progress.
- Leader can become a bottleneck.

---

# Blockchain Consensus Mechanisms

## 1. Proof of Work (PoW)

- Used in Bitcoin and other blockchains.

## How it works

- A node (miner) solves a complex mathematical puzzle.
- The solution (proof) is verified by other nodes.
- If valid, the block is added to the blockchain.

## Pros

- Highly secure.
- Resistant to attacks.

## Cons

- Slow.
- Consumes huge amounts of energy.

---

## 2. Proof of Stake (PoS)

- Used in Ethereum 2.0 and modern blockchains.
- Validators stake cryptocurrency instead of solving puzzles.

## How it works

- Validators lock coins as stake.
- Validators are selected to validate blocks based on stake and protocol rules.
- Malicious validators may lose their stake.

## Pros

- Energy efficient.
- Faster than PoW.

## Cons

- More complex economic design.
- Potential wealth centralization.

---

# Leader Election vs Consensus

## Leader Election

- Leader election is the process of selecting a coordinator among nodes.
- It answers the question:

```txt
Which node should coordinate work?
```

- It does not by itself guarantee agreement on a value.

---

## Consensus

- Consensus is the process of agreeing on a value or order across nodes.
- It answers the question:

```txt
What value should the system agree on?
```

- It ensures all non-faulty nodes eventually learn the same result.

---

## Relationship Between Them

- In many systems, leader election is a supporting mechanism for consensus.

### Example

- Raft elects a leader first.
- The leader then coordinates log replication and consensus.

- Some algorithms do not require a permanently fixed leader.

### Example

- Paxos can operate without a stable leader, although practical implementations often use one for efficiency.
- PBFT can also achieve consensus without a single long-term leader.
