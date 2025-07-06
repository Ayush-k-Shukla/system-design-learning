# 🧩 Things to Know When Building Microservices

1. Microservice architecture (MSA) creates small modular services which belong to a single application.
2. One major advantage of MSA is that now independent services can be re-written in different languages which are best for each service.
3. We can deploy one service without affecting others as long as the external interface (API contract) is not modified.
4. **Issues and Resolutions**

   1. **Nano Service Hell**

      1. Services should be like “Billing”, “Payment” service, not like “getUserById”, “getUser” service.
      2. The point is: don’t build too many small services that outweigh the benefits.

   2. Use OpenAPI spec to keep the API contract relevant and up to date.
   3. Use service discovery tools for endpoint finding based on environment.
   4. We can automate deployment jobs using Jenkins.
   5. One service fails, everything fails (Ways to mitigate):

      1. Retries – have a retry logic with a fixed number of times, use exponential backoff between tries.
      2. Circuit breaker
         1. If a service continuously returns errors, then we need to make a circuit breaker that reduces bombarding requests.
            ![https://i1.wp.com/cloudncode.files.wordpress.com/2016/07/circuit-breaker.png](https://i1.wp.com/cloudncode.files.wordpress.com/2016/07/circuit-breaker.png)

   6. **Database per Service**

      1. Allows flexibility to deploy, elastic search independently per service.

   7. **Schema per Service**

      1. Promotes loose coupling.

   8. **Data Warehousing**

      1. For each service, we have a different DB used, so for analytical purposes we can choose to have each service push data in a given format to a data warehouse.

   9. **Too Many Servers, Too Much Cost**

      1. Yes and no. It is true that a monolith service would consume server resources more efficiently and will be easier to write deployment scripts for. However, where MSA really shines is the reduction of everyday costs for development and faster time to market by employing one-time extra setup costs, which is one of the major reasons to go for MSA to begin with.

   10. **Every Service Has to Support All Transport Protocols and Authentication**
       1. It is a common requirement for all services to support common protocols like HTTP and AMQP (Advanced Message Queuing Protocol). Also, services which are exposed publicly need to support authentication and authorization.
       2. This is usually done using some common library which reduces code duplication.
       3. The solution for this problem is to use an API gateway, which is simply a proxy that is aware of all the services registered with it. It takes care of supporting all protocols and authentication and communicating with services in a common protocol (mostly HTTP).
          ![Image](https://i2.wp.com/cloudncode.files.wordpress.com/2016/07/api-gateway.png)
       4. Some popular gateways are Apigee, Mashery, and Amazon API Gateway.

---

### 🔍 Service Discovery

1. In a scalable application, there can be dozens of services which scale up and down dynamically, thus we need a way to communicate with these.
2. Service discovery is a mechanism that allows services to find and communicate with each other in a distributed system.
   e.g. ![Image](/img/hld/service-dis.jpg)
3. A service registry typically stores:
   1. **Basic:** service name, IP, port, status
   2. **Metadata:** version, environment, region, tag
   3. **Health:** status, last health check done on
   4. **Secure Communication:** certificates, protocols
4. **Benefits**
   1. Reduced manual config
   2. Lower risk of failure
   3. Improved scalability
5. **Service Registration Options**
   1. **Manual Registration**
      1. Done by dev manually, not favored from a scalability point of view.
   2. **Self Registration**
      1. Whenever a service starts, it registers itself to the registry by sending an API call with all details (name, port, IP, etc.)
      2. To ensure the registry is up to date, the service sends a [heartbeat](Uncategorized/Heartbeat.md) signal periodically to let the registry know it is active and healthy.
   3. **Third Party Registration**
      1. Another process handles service registration.
      2. Follows the `sidecar` pattern.
   4. **Automatic Registration by Orchestrators**
      1. In modern orchestrated environments like Kubernetes, service registration happens automatically. The orchestration platform manages the lifecycle of services and updates the service registry as services start, stop, or scale.
6. **Types of Service Discovery**
   1. **Client Side**
      1. Here, all responsibility is on the client (usually a microservice or API gateway) to query the registry and route accordingly.
      2. The registry responds with all instances of the requested service and then it's on the client to route, usually based on a load balancing algorithm.
      3. It is easy to implement and reduces load on the central load balancer.
      4. Netflix's open-source library **Eureka** is a popular tool for this.
   2. **Server Side**
      1. Here, a central load balancer handles all routing and logic.
      2. Instead, the client simply sends a request to a central server (load balancer or API gateway), which handles the rest.
