# Uber Eats Image Deduping & Storage Recap

[👉 Article Link](https://www.uber.com/en-IN/blog/deduping-and-storing-images-at-uber-eats/)

## Precontext: Content Addressable Caching

- Content-addressable caching (or content-addressable storage + caching) is a technique where the content itself determines the key used to store and retrieve it.
- The core idea is Instead of identifying data by (URL, filename or ID) we use a hash of the content (like SHA-256, MD5, etc).
- So the address = hash(content).

## Why?

- Uber Eats handles **100M+** images.
- Many merchants upload **identical product images** means big
  duplicate storage.
- Frequent updates can cause repeated downloads, processing, and CDN
  usage.
- Goal: reduce **storage cost**, **processing load**, and **latency**.

## Idea: Content‑Addressable Storage

we built a deduplication layer based on **image hashes**.

### Three Metadata Maps

- maps are usually caches but backed by DBs so survive restarts
- main image sources is S3 like blob storage of uber itself

| Map Name                | Key                          | Value                     | Why                                                                                                                               |
| ----------------------- | ---------------------------- | ------------------------- | --------------------------------------------------------------------------------------------------------------------------------- |
| **URL Map**             | Image URL                    | Hash of image             | To avoid **re-downloading** the same external URL again; detects repeated URLs and checks whether the underlying image changed.   |
| **Original Image Map**  | Image Hash                   | Raw / original image      | To deduplicate **identical images** uploaded via different URLs; many merchants may use same product image → only store one copy. |
| **Processed Image Map** | Image Hash + Processing Spec | Processed / resized image | To avoid **re-processing** the same image in different sizes/formats; store and reuse thumbnails, WebP versions, etc.             |

### Processing Flow

![https://lh4.googleusercontent.com/KQRWRxdD8P4xiDcARfsjRiBah_FHtja7sJ8m65BJF3s-g_98cZWn4uR9I3iF0-LnXvIePcfn2SJC5hDo33gRG71kgGszq70iEZ18KbBH1JFSEMh7swlAw9-Q0x6WFU0yP80iHR0g2lw1-RkElD0niYpZO-UXOS1oDYM5onTJ91pzevoXiiQkyyU-](https://lh4.googleusercontent.com/KQRWRxdD8P4xiDcARfsjRiBah_FHtja7sJ8m65BJF3s-g_98cZWn4uR9I3iF0-LnXvIePcfn2SJC5hDo33gRG71kgGszq70iEZ18KbBH1JFSEMh7swlAw9-Q0x6WFU0yP80iHR0g2lw1-RkElD0niYpZO-UXOS1oDYM5onTJ91pzevoXiiQkyyU-)

1.  Given (URL + processing spec), check **URL map**.
2.  If URL seen → get hash; else download → hash → store.
3.  Check **Processed Image Map**.
4.  If processed variant exists → return cached version.
5.  Else process → store → return.

### Update Handling

![https://lh5.googleusercontent.com/pg05FCtFB0we0WDHiLzklT2AUEDmfTIC1K1YvOxT5KjHRJWtukFt0TZgxDN97qgyLN-cESclFz3TwD40Ag_KkMBKGaHo7h0vSpxWErffoFNfCQyY5KevlMHYzuVf9k99wgFhqaCjp22Oe-8ln7WNs3Y_eUAULK5ohGwL6MGkdbUI14SgytVuiI_3](https://lh5.googleusercontent.com/pg05FCtFB0we0WDHiLzklT2AUEDmfTIC1K1YvOxT5KjHRJWtukFt0TZgxDN97qgyLN-cESclFz3TwD40Ag_KkMBKGaHo7h0vSpxWErffoFNfCQyY5KevlMHYzuVf9k99wgFhqaCjp22Oe-8ln7WNs3Y_eUAULK5ohGwL6MGkdbUI14SgytVuiI_3)

- Uses HTTP **Last-Modified** header to detect changed images.
- If unchanged, skip re-download and reuse existing blobs.

### Error Caching

- Processing errors (e.g., corrupt image, too small) are also cached.
- Prevents repeated unnecessary attempts.

## Value

- Latency improved: **P50 \~100ms**, **P90 \~500ms**.
- less than 1% of weekly calls needed actual image processing.
- Huge savings in storage + CDN + CPU usage.

## Key learnings

- Hash-based **content-addressable storage** eliminates duplication.
- Separate raw images from processed variants for flexibility.
- Use metadata maps for fast lookups.
- Cache both successes and failures.
- Use HTTP metadata to detect updates cheaply.
