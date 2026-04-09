# Design a Web Crawler

## Overview

A **web crawler** (also called a _robot_ or _spider_) is used to discover and collect content from the web such as:

- Web pages
- Images
- Videos
- PDFs

It starts with a set of URLs and recursively follows links to discover more content.

---

## Use Cases

### 1. Search Engine Indexing

- Build searchable index of web pages
- Example: Googlebot

### 2. Web Archiving

- Preserve web data for future use

### 3. Web Mining

- Extract insights (e.g., financial reports)

### 4. Web Monitoring

- Detect copyright or trademark violations

---

## Basic Algorithm

1. Start with seed URLs
2. Download web pages
3. Extract URLs from pages
4. Add new URLs to queue
5. Repeat

---

## 🧠 Step 1: Requirements & Scope

- Purpose: Search engine indexing
- Scale: 1 billion pages/month
- Content: HTML only
- Handle updates: Yes
- Storage: 5 years
- Deduplication: Required

---

## Characteristics of a Good Crawler

- Scalability
- Robustness
- Politeness
  - Meaning not getting rate-limited
- Extensibility
  - for now we are supporting html but we should be easily able to extend for image/vedios as well

---

## Back-of-the-Envelope Estimation

- QPS ≈ 400
- Peak QPS ≈ 800
- Storage (5 years) ≈ 30 PB

---

## High-Level Design Components

- Seed URLs
- URL Frontier
- HTML Downloader
- DNS Resolver
- Content Parser
- Content Seen (Deduplication)
- Content Storage
- URL Extractor
- URL Filter
- URL Seen
- URL Storage

---

## Workflow

<p align="center">
   <img src="/img/hld-questions/crawler/flow-crawler.svg" />
</p>

1. Add seed URLs → URL Frontier
2. Fetch URLs → Downloader
3. Resolve DNS → Download HTML
4. Parse content
5. Check duplicate content
6. Store if new
7. Extract links
8. Filter URLs
9. Check if URL seen
10. Add new URLs to frontier

---

## Deep Dive

### BFS vs DFS

- BFS preferred
  - as we pick this as because of FIFO order and also help in implementation
  - we also keep in mind priority of webpage based on page rank, traffic and all
- DFS can go too deep

---

### URL Frontier Goals

It is a data structure which stores urls to be downloaded next

- Politeness
- Priority
- Freshness

---

### Politeness

- Avoid too many requests per host
- Use host-based queues

---

### Priority

- Based on importance (PageRank, traffic)
- Multiple priority queues

---

### Freshness

- Recrawl based on update frequency

---

### Storage Strategy

- Disk for scale
- Memory for speed

---

## HTML Downloader

### Robots.txt

- Respect crawl rules
- Cache results

---

### Performance Optimizations

- Distributed crawling
- DNS caching
- Locality
- Timeouts

---

## Handling Problematic Content

### Duplicate Content

- Use hashing

### Spider Traps

- Limit URL depth
- Detect abnormal patterns

### Data Noise

- Filter spam, ads
