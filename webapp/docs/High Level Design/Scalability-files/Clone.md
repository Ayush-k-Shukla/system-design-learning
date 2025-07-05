# Clone

1. Public servers of a scalable web service are hidden behind a load balancer.
2. Sessions must be stored in a centralized data store accessible to all your application servers. It can be an external database or persistent cache, like Redis.
3. To ensure that the same updated code is running on same server we can use multiple tools and one of them is the docker