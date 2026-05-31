# Tokenization and Embeddings

## 1. The Core Architecture Pipeline

```
[ Raw Text Input ]  e.g., "Tokenization is fun"
        │
        ▼
┌────────────────────────────────────────────────────────┐
│ 1. Tokenizer (Independent Statistical Rule Engine)     │
│    - Splits text into string pieces (subwords/bytes)   │
│    - Maps string pieces to unique vocabulary Indices   │
└────────────────────────────────────────────────────────┘
        │
        ▼
[ Token IDs List ]  e.g., [34201, 318, 1204]
        │
        ▼  ================== LLM BOUNDARY ==================
        │
┌────────────────────────────────────────────────────────┐
│ 2. Embedding Layer (First Parameter Layer of LLM)     │
│    - Performs high-speed lookup on Embedding Matrix     │
│    - Matrix Dimension: (Vocabulary Size × Hidden Dim)   │
│    - Converts static ID into uncontextualized vector   │
└────────────────────────────────────────────────────────┘
        │
        ▼
[ Dense Embedding Vectors ]  e.g., Shape: [Sequence Length × 4096]
        │
        ▼
┌────────────────────────────────────────────────────────┐
│ 3. Transformer Layer Stack (Attention & Feed-Forward)  │
│    - Multi-Head Self-Attention evaluates context       │
│    - Dynamically shifts vector coordinates based on    │
│      surrounding tokens (e.g., "Apple" fruit vs. tech) │
└────────────────────────────────────────────────────────┘
        │
        ▼
[ Next-Token Probability Distribution / Model Output ]
```
