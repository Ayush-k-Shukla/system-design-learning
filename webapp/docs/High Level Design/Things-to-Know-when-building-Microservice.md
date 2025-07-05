# Things to Know when building Microservice

1. Micro service architecture (MSA) creates small modular services which belong to single application
2. One major advantage of MSA is like now independent services can be re-written in different language which is best for service.
3. we can deploy as well one service without affecting other as long as external interface (API contract) is not modified.
4. **Issues and resolutions**

   1. **Nano service hell**

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

   6. **Database per service**

      1. allows flexibility to deploy , elastic search independently per service

   7. **Schema per service**

      1. promote loose coupling

   8. **Data warehousing**

      1. like for each service we have different DB used so for analytical purpose we can choose to give to each service to push data in given format to data house.

   9. **Too many servers, too much cost**

      1. Yes and no, it is true that a monolith service would consume server resources more efficiently and will be easier to write deployment scripts for, however, where MSA really shines is the reduction of everyday costs for development and faster time to market by employing one time extra setup costs which is one of the major reasons to go for MSA to begin with.

   10. **Every service has to support all transport Protocol and authentication**

       1. It is a common requirement for all services to support the common protocol like HTTP and AMQP (Advanced Message Queuing Protocol). and also services which are exposed publicly need to support the Authentication and Authorization.
       2. This is usually done using some common library which reduce code duplication.
       3. Solution for this problem is to use a API gateway which is simply a proxy which is aware of all the services registered with it. It take care os supporting all protocols and authentication and communicating with services in a common protocol (mostly HTTP).

          ![Image](https://i2.wp.com/cloudncode.files.wordpress.com/2016/07/api-gateway.png)

       4. Some popular gateways are Apigee, Mashery and Amazon API Gateway

### Service Discovery

1. As in a scalable application there can be dozens of service which scale up and down dynamically thus we need a way to communicate with these.
2. Service Discovery is a mechanism that allows services to find and communicate with each other in a distributed system
   e.g. ![Image](/img/hld/service-dis.jpg)
3. A service registery typically stores
   1. **Basic:** service name, IP, port, status
   2. **Metadata:** version, environment, region, tag
   3. **Health:** status, last health check done on
   4. **Secure Communication:** certificates, protocola
4. **Benefits**
   1. Reduced Manual config
   2. Risk of failure
   3. Improved scalability
5. **Service Registration Options**
   1. **Manual Registration**
      1. Done by dev manually, not favoured in scalability point of view.
   2. **Self Registrations**
      1. when ever a service starts it register itself to the registery by sending api with all details(name, port, ip,..etc)
      2. To insure registery upto date, service sends [heartbeat](Uncategorized/Heartbeat.md) signal periodically to let registry know it is active and healthy
   3. **Third Party Registration**
      1. Another process handles service registration.
      2. follows `sidecar` pattern.
   4. **Automatic Registration by Orchestrators**
      1. In modern orchestrated environments like Kubernetes, service registration happens automatically. The orchestration platform manages the lifecycle of services and updates the service registry as services start, stop, or scale
6. **Types of Service Discovery**
   1. **Client side**
      1. Here all responsibility is of client (usually a microservice or API gateway) to query registery and route accordingly.
      2. registery responds with all instances of requested service and then it's on client to route based on usually load balancing algo.
      3. It is easy to implement and reduces load on central LB
      4. Netflix's open-source lib **Eureka** is a popular tool for this.
   2. **Server side**
      1. Here a central load balancer handles all routing and logic.
      2. Instead, the client simply sends a request to a central server (load balancer or api gateway), which handles the rest.
