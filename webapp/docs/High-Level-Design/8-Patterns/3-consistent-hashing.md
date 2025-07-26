# 🔄 Consistent Hashing

<p align="center">
 <img src="/img/hld/image3.png" />
</p>

**Consistent Hashing** is a technique to distribute data across a dynamic set of nodes (servers) in a way that minimizes reorganization when nodes are added or removed.

1. In a traditional modulo-based system, scaling up or down requires remapping most of the data, which is inefficient.
2. In consistent hashing, both nodes (servers) and keys (requests) are mapped onto a ring using a hash function. When a request arrives, we move clockwise on the ring to find the first server, which will handle the request.

---

## Virtual Nodes and Replicas

To avoid uneven data distribution and overloads (hotspots), each physical node is assigned multiple positions on the ring, called **virtual nodes**. This ensures a more uniform distribution of keys.

- Instead of using multiple hash functions (which is hard to design for uniformity), generate K replica IDs for each server (e.g., server1-1, server1-2, ..., server1-K) and hash these to get K points on the ring.
- If a node fails, its virtual nodes' data is redistributed to the next nodes, reducing the risk of overload.

This approach ensures even data spread and resilience to node failures.

---

## ➕ Adding or Removing Nodes

**Adding a Node:**

1. Find the new node's position(s) in the hash space (ring).
2. Populate the new node with the data it should serve (keys between its position and the previous node).
3. Only the keys in the segment between the new node and its predecessor are remapped; all other keys remain unaffected.
4. On average, only `k/n` keys need to be moved, where `k` is the total number of keys and `n` is the number of nodes.

**Removing a Node:**

1. Find the node's position(s) in the hash space.
2. Move its data to the next node(s) on the ring.
3. Only the keys associated with the removed node are affected; the rest remain unchanged.

This minimizes data movement and remapping during scaling operations.

---

## 🚀 Use Cases

- Consistent hashing minimizes data movement when nodes join or leave.
- Used in distributed caching (e.g., Memcached), databases, load balancers, P2P networks, and decentralized storage systems.

---

## ⚠️ Edge Cases & Solutions

**Uneven Distribution of Keys:**

- If nodes are not evenly distributed, some nodes may become hotspots (overloaded).
- Solution: Use virtual nodes to ensure even distribution and prevent hotspots.

**Single Node Failure:**

- If a node fails, its data is reassigned to the next node, which can cause overload.
- Solution: Virtual nodes help distribute the load more evenly in case of failures.
