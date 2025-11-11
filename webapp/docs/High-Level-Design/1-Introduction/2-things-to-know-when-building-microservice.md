# 🧩 Things to Know When Building Microservices

- Microservice architecture (MSA) creates small modular services which belong to a single application.
- One major advantage of MSA is that now independent services can be re-written in different languages which are best for each service.
- We can deploy one service without affecting others as long as the external interface (API contract) is not modified.

## Issues and Resolutions

### Nano Service Hell

- Services should be like “Billing”, “Payment” service, not like “getUserById”, “getUser” service.
- The point is: don’t build too many small services that outweigh the benefits.

### Use OpenAPI spec to keep the API contract relevant and up to date.

### Use service discovery tools for endpoint finding based on environment.

### We can automate deployment jobs using Jenkins.

### One service fails, everything fails (Ways to mitigate):

- **Retries** – have a retry logic with a fixed number of times, use exponential backoff between tries.
- **Circuit breaker**
  - If a service continuously returns errors, then we need to make a circuit breaker that reduces bombarding requests.
  - ![https://i1.wp.com/cloudncode.files.wordpress.com/2016/07/circuit-breaker.png](https://i1.wp.com/cloudncode.files.wordpress.com/2016/07/circuit-breaker.png)

### Database per Service

- Allows flexibility to deploy, elastic search independently per service.

### Schema per Service

- Promotes loose coupling.

### Data Warehousing

- For each service, we have a different DB used, so for analytical purposes we can choose to have each service push data in a given format to a data warehouse.

### Too Many Servers, Too Much Cost

- Yes and no. It is true that a monolith service would consume server resources more efficiently and will be easier to write deployment scripts for. However, where MSA really shines is the reduction of everyday costs for development and faster time to market by employing one-time extra setup costs, which is one of the major reasons to go for MSA to begin with.

### Every Service Has to Support All Transport Protocols and Authentication

- It is a common requirement for all services to support common protocols like HTTP and AMQP (Advanced Message Queuing Protocol). Also, services which are exposed publicly need to support authentication and authorization.
- This is usually done using some common library which reduces code duplication.
- The solution for this problem is to use an API gateway, which is simply a proxy that is aware of all the services registered with it. It takes care of supporting all protocols and authentication and communicating with services in a common protocol (mostly HTTP).
  - ![Image](https://i2.wp.com/cloudncode.files.wordpress.com/2016/07/api-gateway.png)
- Some popular gateways are Apigee, Mashery, and Amazon API Gateway.

---

## 🔍 Service Discovery

- In a scalable application, there can be dozens of services which scale up and down dynamically, thus we need a way to communicate with these.

- Service discovery is a mechanism that allows services to find and communicate with each other in a distributed system.

- e.g. ![Image](/img/hld/service-dis.jpg)

### A service registry typically stores:

- **Basic:** service name, IP, port, status
- **Metadata:** version, environment, region, tag
- **Health:** status, last health check done on
- **Secure Communication:** certificates, protocols

### Benefits

- Reduced manual config
- Lower risk of failure
- Improved scalability

### Service Registration Options

- **Manual Registration**
  - Done by dev manually, not favored from a scalability point of view.
- **Self Registration**
  - Whenever a service starts, it registers itself to the registry by sending an API call with all details (name, port, IP, etc.)
  - To ensure the registry is up to date, the service sends a [heartbeat](Uncategorized/Heartbeat.md) signal periodically to let the registry know it is active and healthy.
- **Third Party Registration**
  - Another process handles service registration.
  - Follows the `sidecar` pattern.
- **Automatic Registration by Orchestrators**
  - In modern orchestrated environments like Kubernetes, service registration happens automatically. The orchestration platform manages the lifecycle of services and updates the service registry as services start, stop, or scale.

### Types of Service Discovery

- **Client Side**
  - Here, all responsibility is on the client (usually a microservice or API gateway) to query the registry and route accordingly.
  - The registry responds with all instances of the requested service and then it's on the client to route, usually based on a load balancing algorithm.
  - It is easy to implement and reduces load on the central load balancer.
  - Netflix's open-source library **Eureka** is a popular tool for this.
- **Server Side**
  - Here, a central load balancer handles all routing and logic.
  - Instead, the client simply sends a request to a central server (load balancer or API gateway), which handles the rest.
