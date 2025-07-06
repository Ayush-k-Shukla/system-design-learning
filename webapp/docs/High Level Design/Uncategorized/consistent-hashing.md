# 🔄 Consistent Hashing

<p align="center">
 <img src="/img/hld/image3.png" />
</p>

1. In a normal (modulo) system, if we try to scale down or up, we need to update the system space again, which requires a lot of time and resources.

2. Here, the nodes (servers) and users (requests) are put on the same ring, and whenever a request comes, we traverse clockwise. Whichever server is found first will serve the request.

---

## ➕ Adding a New Node

1. When scaling up and adding a node to the system, we do these steps:
   1. Find the position of the node where it resides in the hash space.
   2. Populate the new node with the data it is supposed to serve.
   3. Add the node in the hash space.
2. When a new node is added, it only affects the files that hash at the location to the left and are associated with the node to the right.
3. All other files and associations remain unaffected, thus minimizing the amount of data to be migrated and mapping required to be changed.
4. Consistent Hashing on average requires only `k/n` units of data to be migrated during scale up and down, where `k` is the total number of keys and `n` is the number of nodes in the system.

---

## ➖ Removing a Node

1. When there is a need to scale down and remove an existing node from the system:
   1. Find the position of the node to be removed from the hash space.
   2. Populate the node to the right with data that was associated with the node to be removed.
   3. Remove the node from the hash space.
2. When a node is removed from the system, it only affects the files associated with the node itself. All other files and associations remain unaffected, thus minimizing the amount of data to be migrated and mapping required to be changed.

---

## 🔗 Associating an Item to a Node

...existing code...

---

## 🚀 Use Cases

1. Consistent hashing minimizes data movement when nodes join or leave.
2. Real-world applications include caching, databases, load balancing, P2P networks, and decentralized storage.

---

## ⚠️ Edge Case Handling

1. **Uneven Distribution of Keys**

   1. **Problem**
      1. If nodes are not evenly distributed, then one node can have more data than others.
      2. This can create hotspots, where some nodes get overloaded while others remain underutilized.
   2. **Solution**
      1. Instead of assigning a single position to each node, each node gets multiple positions (virtual nodes) in the ring.
      2. This ensures that data is evenly spread across nodes, preventing hotspots.

2. **Single Node Failure**
   1. If one node fails, all data of that node will go to the next node, and this can lead to overload.
   2. The solution for this problem is also **virtual nodes**.
