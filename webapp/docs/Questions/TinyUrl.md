# TinyUrl

## High level design

<p align="center">
   <img src='/img/hld-questions/tinyurl-hld.png'/>
</p>

## Estimation

<p align="center">
   <img src='/img/hld-questions/tinyurl.drawio.svg'/>
</p>

## Requirement Gathering

1. Functional
   1. Generate long to short url
   2. Redirection
   3. Support link expiration
   4. Clicks monitoring (Optional)
   5. Allow aliasing (Optional)
2. Non Functional
   1. High availability (99.9%)
   2. Scalability

## Deep dive into components

### URL generation service

1. It will handle generation of unique URLs.
2. Handle collisions.
3. **Approach 1 for URL generation : Hashing and Encoding**
   1. Convert `long_url` into a hash using `MD5` or `SHA-256`
   2. Encode that hash into URL friendly `Base62`.
   3. Not take few bytes to represent short url.
   4. Collision handeling
      1. Make DB query before hand
      2. Make short_url as unique and rely on the DB contraint to fail and on failure append some increment suffix(like `-1`,`-2`).
4. **Approach 1 for URL generation : Incremental IDs**
   1. instead of hashing we can rely on Database incremental ids.
   2. Convert id into url friendly using base62.
   3. Consideration
      1. DB dependency can be bottleneck in scaling
      2. Harder for distributed systems to implement
5. **Custom Alias**
   1. This we can allow based on whether same alias is present in DB or not.
   2. Validate character in alias to be URL frendly
   3. if alias already taken return correct errors
6. **Link expiration**
   1. Can be done using running a `cron job`, which removed expired links from DB
   2. During the redirection process service can check if link is expired return to a default page and show error.

### Redirection service

1. DB lookup and then redirection
2. send HTTP redirect response (301)
3. For performance can cache in-memory.

### Analytics Service

1. Use a message Queue like Kafka to track each event click.
2. Batch process agreegation of data.

## Key Issues and Bottlenecks

### Scalability

1. Deploy `API layer` across multiple instances behind load-balancer to distrivute incoming requests evenly.
2. **Sharding**
   1. If we are using auto increment id then we can shard based on range.(0-10k,...)
   2. Other case we can use Hash based sharding. When adding more shards we may need to use consistent hashing to handle.
3. **Caching**
   1. store frequently accessed urls.

### Availability

1. DB replications (Single master)
2. Geo distributed Deploy

### Security

1. Rate limiting
2. input validation
3. HTTPS
