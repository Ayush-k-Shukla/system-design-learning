# Vector Database Indexes

## Introduction to Vector Indexing

When building applications with Large Language Models (LLMs), RAG, or semantic search systems, high-dimensional vector embeddings (typically ranging from `256` to `1536+` dimensions) are generated. Comparing a query vector against millions of stored vectors using an exact brute-force search requires `O(N)` time complexity. This creates a massive scaling bottleneck, grinding production systems to a halt.

**Vector database indexes** solve this by organizing high-dimensional vector spaces so you can perform **Approximate Nearest Neighbor (ANN)** searches. Instead of checking every single record, these indexes trade a tiny, controllable fraction of accuracy (recall) for massive, orders-of-magnitude increases in search speed (`O(log N)` or `O(1)` amortized time).

---

## 1. Flat Index (No Index / Brute Force)

It isn't actually an "approximate" index. It stores vectors exactly as they are without any structural modification, partitioning, or clustering.

### How it is Implemented

- **Storage:** Vectors are stored as continuous, sequential arrays of floating-point numbers (`FP32`, `FP16`) in memory or on disk.
- **Search Mechanism:** When a query arrives, the system calculates the exact distance (e.g., Cosine similarity, Euclidean distance, or Dot Product) between the query vector and **every single vector** in the entire database.

### Problem it Solves

- Eliminate information loss and recall degradation.
- If you Need to find _exact_ mathematically nearest neighbors.

### Pros & Cons

- **Pros:**
  - `100%` perfect recall (perfect accuracy).
  - Zero build/training time.
  - Inserting new vectors is instantaneous (`O(1)`).
- **Cons:**
  - Search time scales linearly with data size (`O(N)`).
  - Unusable for production apps with thousands or millions of vectors.
  - Extremely CPU/GPU intensive during queries.

### Search Speed & Latency Impact

- **Access Speed Complexity:** `O(N * D)`, where `N` is the number of vectors and `D` is the dimensionality.
- **Real-world feel:** Latency scales linearly with vectror size

### Use Cases & Design Patterns

- **Small Datasets:** Collections with fewer than 10,000 vectors.
- **Ground Truth Evaluation:** Used as a golden baseline benchmark to evaluate the accuracy (recall) of other ANN indexes.

---

## 2. Inverted File Index (IVF)

IVF is a space-partitioning index designed to narrow down the search space by grouping similar vectors together into local buckets before running queries.

### How it is Implemented

- **Training Phase (K-Means):** It uses the **K-Means Clustering** algo to partition the high-dimensional vector space into `K` distinct clusters. Each cluster is anchored by a central point called a **centroid**.
- **Inverted List Structural Pattern:** It maps each unique centroid ID to an inverted list containing the IDs and values of all vectors belonging to that specific cluster.
- **Search Mechanism:** When a query vector arrives, the system first compares it against the `K` centroids to find the closest ones. It then _only_ searches the vectors inside those specific closest clusters, entirely skipping the rest of the database.

### Deeper Dive: K-Means Clustering Core Mechanics

- **Initialization:** The database selects `K` random vectors from the dataset to act as the initial centroids.
- **Assignment Phase:** The algorithm iterates through every single vector in the training set, calculates its distance to all `K` centroids, and assigns the vector to its mathematically nearest centroid.
- **Update Phase:** For each of the `K` clusters, the algorithm calculates the mean vector of all data points currently assigned to it. This new mean vector becomes the updated centroid.
- **Convergence:** Steps 2 and 3 repeat iteratively until the centroids stop moving significantly (or a maximum iteration threshold is hit).

This process carves the high-dimensional space into geometric regions called **Voronoi cells**. Every point inside a specific Voronoi cell is closer to that cell's centroid than to any other centroid in the database.

### Deeper Dive: The `nprobe` Parameter (The Speed vs. Accuracy Knob)

`nprobe` is a dynamic, **query-time configuration parameter** used exclusively with IVF indexes. It dictates how many of the nearest clusters (Voronoi cells) the database will open up and search during a query.

- **The Edge Effect Problem:** If your query vector lands near the boundary/edge of a cluster, its true nearest neighbors might actually reside just across the border in an adjacent cluster.
- **If `nprobe = 1`:** The database looks _only_ inside the single closest cluster. If your true nearest neighbor is across the border, you miss it, resulting in lower recall.
- **If `nprobe = K`:** The database searches _all_ clusters, transforming the IVF index right back into a slow, brute-force Flat index.
- **The Trade-off:** Low `nprobe` (e.g., 1 to 5) gives blazing-fast latency but lower recall accuracy. High `nprobe` (e.g., 32 to 128+) gives high recall accuracy but latency increases linearly with the number of vectors checked.

<p align="center">
   <img src="/img/hld/ivf.svg" />
</p>

<p align="center">
   <img src="/img/hld/ivf-storage.svg" />
</p>

- How Search is performed

```
                    [ Query Vector ]
                              │
               ┌──────────────┴──────────────┐
               ▼                             ▼
     [ Compute Distances to All Centroids in Registry ]
               │
               ▼
     [ Rank Centroids by Distance: C2 (Closest), C1 (2nd Closest), C3 (Far) ]
               │
      ┌────────┴────────┬───────────────────┐
      │ (If nprobe=1)   │ (If nprobe=2)     │ (If nprobe=3)
      ▼                 ▼                   ▼
[ Search List C2 ]   [ Search Lists ]    [ Search Lists ]
                     [   C2 and C1  ]    [ C2, C1, and C3]
```

### Problem it Solves

It drastically cuts down the number of vector comparisons from `N` to a tiny fraction of `N` by eliminating irrelevant geometric regions early in the query lifecycle.

### Pros & Cons

- **Pros:**
  - Significantly reduces search latency compared to Flat indexes.
  - Low memory footprint compared to graph-based structures.
- **Cons:**
  - Requires a slow, computationally intensive "training/build" step.
  - If your data distribution shifts significantly over time (data drift), you must completely re-index.
  - Vulnerable to lower recall if `nprobe` isn't carefully tuned.

### Search Speed & Latency Impact

- **Access Speed Complexity:** Reduces search complexity down to `O(K + nprobe*(N/K))`.
- **Real-world feel:** Sub-10ms search times even on millions of rows, depending on your chosen trade-off balance.

### Use Cases & Design Patterns

- **Memory-Constrained Production Systems:** When you have millions of vectors but want to keep the active index memory footprint small and low-cost.
- **Partitioned Multitenancy:** Excellent when you can cluster data naturally by customer ID or data category, pairing structural metadata filtering with IVF lists.

---
