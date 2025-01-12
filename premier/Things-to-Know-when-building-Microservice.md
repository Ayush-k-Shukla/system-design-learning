# Things to Know when building Microservice

1. Micro service architecture (MSA) creates small modular services which belong to single application
2. One major advantage of MSA is like now independent services can be re-written in different language which is best for service.
3. we can deploy as well one service without affecting other as long as external interface (API contract) is not modified.
4. Issues and resolutions

   1. Nano service hell
      1. it should be like “Billing”, “Payment” service not like “getUserById”, “getUser” service
      2. point is don’t build too much small services that outweigh the benefits
   2. Use OpenAPI spec in order to make api contract relevant and up to date
   3. Use Service discovery tools for endpoint finding based on environment
   4. We can automate deployment jobs using jenkins
   5. One service fails, everything fails (Ways to mitigate)

      1. Retries - have a retry logic with fixed number of time, use exponential backoff between tries.
      2. Circuit breaker

         1. if service continuously return error then we need to make a circuit breaker that reduce bombarding request
            ![https://i1.wp.com/cloudncode.files.wordpress.com/2016/07/circuit-breaker.png](https://i1.wp.com/cloudncode.files.wordpress.com/2016/07/circuit-breaker.png)

   6. Database per service
      1. allows flexibility to deploy , elastic search independently per service
   7. Schema per service
      1. promote loose coupling
   8. Data warehousing
      1. like for each service we have different DB used so for analytical purpose we can choose to give to each service to push data in given format to data house.
   9. Too many servers, too much cost
      1. Yes and no, it is true that a monolith service would consume server resources more efficiently and will be easier to write deployment scripts for, however, where MSA really shines is the reduction of everyday costs for development and faster time to market by employing one time extra setup costs which is one of the major reasons to go for MSA to begin with.
   10. Every service has to support all transport Protocol and authentication
       1. It is a common requirement for all services to support the common protocol like HTTP and AMQP (Advanced Message Queuing Protocol). and also services which are exposed publicly need to support the Authentication and Authorization.
       2. This is usually done using some common library which reduce code duplication.
       3. Solution for this problem is to use a API gateway which is simply a proxy which is aware of all the services registered with it. It take care os supporting all protocols and authentication and communicating with services in a common protocol (mostly HTTP).
          ![Image](https://i2.wp.com/cloudncode.files.wordpress.com/2016/07/api-gateway.png)
       4. Some popular gateways are Apigee, Mashery and Amazon API Gateway
