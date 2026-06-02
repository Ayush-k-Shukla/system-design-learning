# Tokenization and Embeddings

## The Core Architecture Pipeline

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

---

## Tokenization: Deep Dive & Taxonomy

Tokenization is the process of mapping text into a sequence of integer IDs belonging to a predefined **Vocabulary (Vocab)**.

### A. Word-Level Tokenization

Splits strings deterministically via whitespaces and punctuation markers. Every distinct word maps to a unique dictionary item.

```
Example: "Let's look at microservices!" -> ["Let's", "look", "at", "microservices!"]
```

- **Pros:**
  - Human-readable, retains semantic boundaries for common words.
- **Cons:**
  - **Out-of-Vocabulary (OOV) Disaster:** Any token absent from the pre-compiled training vocabulary (such as typos, slang, new libraries) is mapped to a generic `<UNK>` token, removing its contextual utility.
  - **Vocabulary Explosion:** Requires tracking millions of permutations (plurals, tenses, prefixes) causing the storage footprint of the input parameter layer to skyrocket.

### B. Character-Level Tokenization

Splits strings down into individual characters (alphabets, symbols, digits).

```
Example: "Tech" -> ["T", "e", "c", "h"]
```

- **Pros:**
  - Mitigates the OOV problem. The vocabulary size is condensed (a few hundred characters at most), allowing it to handle typos, syntax, and foreign alphabets.
- **Cons:**
  - **Sequence Explosion:** An average sentence becomes much longer in characters. Self-attention scales quadratically (`O(N^2)`) with token length, increasing computational cost and reducing the effective context window.
    - Self Attention means a word looks to it's neighbors in order to get the meaning. it will scale bcs A typical subword tokenizer (like BPE) maps roughly 1 word to 0.75 tokens but in this case it will be 1 to 1.
    - It will increase cost of calculating self attention as well as more neighbour scanning.
  - **Semantic Void:** Base characters hold no individual semantic currency. The model must spend layers of capacity simply grouping sequences back into concepts.

### C. Subword-Level Tokenization (The Production Standard)

The optimal standard implemented across modern foundational models. It adaptively retains common words intact while fracturing rare, complex, or unobserved words into meaningful morphemic structural fragments (roots, suffixes, prefixes).

- **Byte-Pair Encoding (BPE)** _(Used by GPT, LLaMA, RoBERTa)_
  - **Mechanism:**
    - Starts at character-level representation.
    - It systematically iterates through a text corpus, computing adjacent token pair frequencies and iteratively merging the highest-frequency pairs until it hits its target vocabulary quota.
  - **Byte-Level BPE Extension:**
    - Modern tokenizers (`tiktoken`) split text directly into raw UTF-8 bytes instead of characters.
    - This allows universal parsing of multi-byte characters, whitespaces, formatting tabs, and complex emojis without breaking.
  - **Cons:**
    - Can struggle with agglutinative languages where words change extensively with suffixes (e.g., Turkish, Finnish).
  - **Pros:**
    - Highly efficient for English, robust handle on whitespace and formatting.

- **WordPiece** _(Used by BERT, DistilBERT)_
  - **Mechanism:**
    - Similar to BPE but handles merges based on probabilistic optimization rather than raw frequency.
    - It selects pair merges that maximize the log-likelihood of the corpus according to a unigram language model (retaining merges that make the final vocabulary highly predictive).
  - **Formatting:**
    - Uses unique substring structural prefixes (such as `##`) to designate mid-word or trailing sub-tokens (e.g., `"playing"` -> `["play", "##ing"]`).
  - **Cons:**
    - Computationally taxing to construct initially because it recomputes likelihood boundaries globally per candidate merge.
  - **Pros:**
    - Mathematically optimization-driven, excels at capturing strict linguistic boundaries.

- **Unigram** _(Used by T5, SentencePiece engine)_
  - **Mechanism:**
    - Operates in reverse. It initializes with an oversized vocabulary containing full words and sentences.
    - Then evaluates token likelihoods and removes the least predictive until reaching the target capacity.
  - **Unique Superpower (Token Regularization):**
    - It computes multiple valid paths for a single word based on probabilities.
    - During **LLM pre-training**, the tokenizer can feed varied sub-word groupings for the same word (e.g., `["un", "believ", "able"]` vs `["unb", "eliev", "able"]`). This behaves as explicit data augmentation, conditioning the inner layers of the LLM to understand that structural shifts retain consistent core semantics. _(Note: This probabilistic variance is locked into the single highest probability path during model inference for consistent output generation)._

---

## Embeddings

Once a tokenizer turns text into raw token IDs (indices), the **Embedding Layer** steps in.

### Why Token IDs Cannot Be Fed Directly Into an LLM

1. **Arbitrary Scale and Rank Trap:**
   1. If Word A is ID `1`, Word B is ID `2`, and Word C is ID `4`, the linear algebra loops of a neural network assume Word C holds four times the value or magnitude of Word A, or that `ID 1 + ID 2 = ID 3`. This introduces artificial numerical bias.
2. **Missing Spatial Coordinates:**
   1. In categorical or scalar ID setups, the relational distance between adjacent integers is always `1`. Thus, the distance between `"King"` (`1`) and `"Queen"` (`2`) appears identical to the distance between `"Queen"` (`2`) and `"Apple"` (`3`), stripping the network of any geometrical topology to evaluate semantic closeness.
3. **The Orthogonal Wall of One-Hot Encoding:**
   1. Representing IDs as isolated vectors containing a single binary `1` (One-Hot arrays) keeps values scale-free, but forces the dot product between any two tokens to equal exactly `0`. It assumes every single concept in human language shares a flat 0% relationship with every other concept.

### The Mathematical Engine of Embeddings

- Embeddings substitute cold integers with a **Dense Vector** — a continuous array of floating-point numbers distributed across a fixed geometric space (known as the **Hidden Dimension Size**).
- Modern production scale establishes these hyperparameter constraints at explicit powers-of-two boundaries optimized for GPU warp alignment, tensor-core memory strides, and high-throughput vector execution:
  - **BERT-Base:** 768 dimensions
  - **Llama-3-8B:** 4,096 dimensions
  - **GPT-3 (175B):** 12,288 dimensions

- Each position in a 4,096-dimensional vector maps to an abstract trait axis. While humans use concepts like "Royalty", "Masculinity", or "Fruitness" to describe these axes, the LLM constructs superimposed, non-linear variables. A single dimension might track past-tense syntax, documentation style, and physical scale at once.

#### Semantic Coordinate Algebra

Because concepts occupy real spatial coordinates inside this geometry, the model handles language using vector mathematics (such as Cosine Similarity):

`Vector(King) - Vector(Man) + Vector(Woman) ~=(approx equal) Vector(Queen)`

---

## Representation Learning: Who Decides the Features?

- The creation of these 4,096 features involves **no human programming, no manual feature engineering, and zero linguistic rules.**

### The Pipeline of Creation

1. **Initialization:** The model establishes an **Embedding Matrix** of size `(Vocabulary Size × Hidden Dimension)` (e.g., `32,000 × 4,096`). At the start of training, this grid is filled with random noise. Words sit in arbitrary positions.
2. **The Prediction Objective:** The LLM is tasked with predicting the missing next token across trillions of web documents (Distributional Semantics).
3. **The Gradient Update:** When the model miscalculates a word prediction, it calculates its error margins and runs **Backpropagation and Gradient Descent** backward through its layers.
4. **Gravitational Mechanics of Language:** Over billions of iterative adjustments, the calculus forces words that share conversational and functional contexts (e.g., `"Java"` and `"TypeScript"`, or `"lion"` and `"leopard"`) to physically migrate toward each other within the 4,096-dimensional map.

Once pre-training concludes, this multi-dimensional map is **frozen**.

---

## Architectural Distinctions: Embedding Weights vs. Transformer Weights

Distinguish between the static lookup layer and the active attention processing matrix. Both are parameter components of the model (e.g., an 8-Billion model), but their tasks are distinct:

### The Embedding Weights (The Static Matrix)

- **Location:** The first input layer.
- **Properties:** A rigid lookup matrix. It possesses no ability to read context.
- **Behavior:** It looks up a single token ID and hands out its baseline, generic, uncontextualized dictionary vector. For the word `"Apple"`, it yields a vector containing attributes for both a fruit and a tech corporation simultaneously because it doesn't know the surrounding context yet.

### The Transformer Layer Stack Weights (The Active Brain)

- **Location:** The subsequent Attention Layers and Feed-Forward Networks.
- **Properties:** The operational compute infrastructure of the model.
- **Behavior:** It receives the static baseline vectors from the embedding layer and calculates cross-token dependencies (Self-Attention). If it reads `"The apple macbook is fast"`, the Attention weights compute the strong directional pull between the token `"macbook"` and `"apple"`. It mathematically shifts the vector coordinates of `"apple"` **in real-time** deeply into the technology cluster, stripping out the fruit components for that specific processing run.

---

## Lifecycle Behavior: Base Models vs. Fine-Tuning

| Phase                                         | State of Embedding Matrix  | Core Functional Behavior                                                                                                                                                                                                                       |
| :-------------------------------------------- | :------------------------- | :--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Base Model Pre-Training**                   | 🔄 Dynamic / Changing      | The matrix adapts continuously across trillions of tokens to learn structural grammar, language semantics, and base world knowledge.                                                                                                           |
| **Standard Fine-Tuning** _(Instruction/RLHF)_ | ❄️ **Frozen**              | The embedding matrix is locked. The tuning adjustments occur strictly within the internal attention layers to change the model's _behavioral style_ (e.g., outputting JSON, formatting tone) without changing baseline vocabulary definitions. |
| **Domain-Specific Fine-Tuning**               | 🔄 Finely Tuned / Unfrozen | The embedding parameters are unlocked to adapt to dense, unobserved terminologies (e.g., niche medical studies, hyper-specific legal briefs) shifting existing base vectors closer to relevant specialized domains.                            |
| **LoRA Fine-Tuning** _(Parameter-Efficient)_  | ❄️ **Frozen**              | The core embedding matrix remains unadjusted. Adaptations are captured in auxiliary low-rank matrix pairs appended alongside attention routes, making it lightweight and fast to train.                                                        |

---

### Verification and Sanity Check Troubleshooting

- **The Leading Space Phenomenon:** Tokenizers treat `" word"` (with a space) and `"word"` (without a space) as distinct items in the vocabulary, yielding different token IDs. Unintended trailing or leading whitespaces in prompt chains can change an LLM's output.
- **The "Strawberry" Letter Blindness:** Because subword tokenizers break words into arbitrary semantic chunks (e.g., `["straw", "berry"]`), the LLM does not natively look at character arrays like `s-t-r-a-w-b-e-r-r-y`. Consequently, tasks demanding precise individual character tracking require explicitly structured thinking loops (Chain-of-Thought) to reconstruct spelling.
