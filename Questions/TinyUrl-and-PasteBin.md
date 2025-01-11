# TinyUrl + PasteBin (WIP)

1. Problem Requirements
   1. Generate unique URLs
   2. track number of clicks
2. Performance consideration
   1. A median URL click will be around 10K and most popular one can be in million
   2. At a time we have to support 1 trillion urls
   3. The average size of paste url can be in KB so (1kb \* 1 trillion = 1TB)
      1. read >> write we have to optimize
3. Link generation
   1. we have to generate unique links all the time so if we take char (a-z, 0-9) then total possible character are 36 so if we take 8 length key then the possible keys will be around 2 trillion. sufficient for our use case.
   2. also for handling hash collisions we have to use probing.
   3. so if key “ab45de12” is taken then next key will be “ab45de13”
4. Assigning URLs : Replication
   1. How we can try to maximize write throughput where possible
   2. we can’t use multi leader replication as then there will be issue if two same key generated written
   3. So we will be using single leader replication
5. Assigning URLs : Caching
   1. We can’t use caching as well then there will be issue of multi leader
      1. when we use cache we first write to cache and then flush it to DB and there issue can be
6. Assigning URLs : Partitioning
   1. It is important for large data, it can helps us to speed up our read and write
   2. we can partition based on short key start like
      1. part1 (a-d)
      2. part2 (e-h)
      3. ….so on
   3. we can also use consistent hashing in cache we need reshuffling
7. Assigning URLs : Single Node
