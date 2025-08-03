# DNS, CDN, Load Balancer, Proxies

## Domain Name System

- Maps a domain name to an IP address.

![image.png](/img/hld/image.png)
![image.png](/img/hld/how-does-dns-resolution-work.webp)

### DNS Resolution Steps

- The browser (client) checks if the hostname to IP address mapping exists in its local cache.
- If not found, the client checks the Operating System (OS) local cache via a system call.
- If still not found, the client makes a DNS request to the Gateway/Router and checks the router's local cache.
- If unsuccessful, the router forwards the request to the Internet Service Provider (ISP) and checks the ISP's DNS cache.
- If the mapping is still not found, the DNS resolver queries the root servers (13 root servers with replicas worldwide).
- The DNS resolver then queries Top Level Domain (TLD) servers (e.g., .com, .org) and Authoritative name servers (e.g., google.com).
- Optionally, the DNS resolver may query Authoritative subdomain servers (e.g., maps.google.com) depending on the query.

## Content Delivery Network (CDN)

- A globally distributed network of connected servers, serving data from a location near the user.
- Generally used for serving static data like images, HTML, CSS, JS, but some CDNs also support dynamic content.

### Types of CDN

#### Pull CDN

- When you add new static data to your app, and a user from a different location tries to access it, the CDN will take time to load the data the first time as it is not present there. When a user accesses data not on the CDN, it pulls and stores it nearby.

#### Push CDN

- Instead of waiting for the CDN to pull content when needed, you upload the entire content to the CDN beforehand. Your pictures, theme files, videos, and other assets are always on CDN servers around the world.

- Setting up a **pull CDN** is generally easy. Once configured, a pull CDN seamlessly stores and updates content on its servers as requested. The data typically stays there for 24 hours if not modified.

  - However, the ease of use can be a drawback. When making changes, you typically don't control how long the pull CDN cache lasts. If you update an image, it might take up to 24 hours to reflect changes, unless you clear the CDN cache.

- The decision on which CDN type to use depends on traffic and downloads. Blogs hosting large downloads (videos, podcasts) may find push CDNs cheaper and more efficient, as content is only updated when pushed. Pull CDNs help high-traffic, small-download sites by keeping popular content on CDN servers. Updates for content are infrequent enough to keep costs lower than push CDNs.

### Benefits of CDN

- Requests fulfilled by your CDN do not go to the origin server.
- Less distance traveled means faster response time.

### Disadvantages of CDN

- Content might be stale if updated before the TTL expires.
- CDN costs could be significant depending on traffic, but should be weighed against costs incurred without a CDN.

## Load Balancer

- Distributes incoming client requests to computing resources such as application servers and databases.
- Effective for:
  - Preventing resource overload
  - Preventing requests from going to unhealthy servers
  - Eliminating single points of failure

### Types of Load Balancers

#### Hardware Load Balancer

- Physical devices designed for high-speed traffic distribution.
- Expensive and used in enterprise setups.
- Example: Citrix ADC

#### Software Load Balancer

- Applications or services that run on standard servers.
- Cost-effective and flexible.
- Example: HAProxy, Nginx

#### Cloud Load Balancer

- Managed service provided by cloud providers.
- Scalable and easy to integrate.
- Example: Google Cloud LB, Amazon Elastic LB

### Types Based on Functionalities

**Layer 4 (Transport Layer) LB**

- Operates at the TCP/UDP layer.
- Routes traffic based on IP addresses and ports.
- Example: AWS Network Load Balancer (NLB).

**Layer 7 (Application Layer) LB**

- Operates at the HTTP/HTTPS layer.
- Makes routing decisions based on URL, headers, cookies, etc.
- Example: AWS Application Load Balancer (ALB).

### Types of Load Balancing Algorithms

**Round Robin**

- A request is sent to the first server in the list.
- The next request is sent to the second server, and so on.
- After the last server in the list, the algorithm loops back to the first server.
- Preferred only when all servers have the same processing capabilities; can cause issues if servers have different capabilities.

**Weighted Round Robin**

- Each server is assigned a weight based on processing power or available resources.
- Servers with higher weights receive a proportionally larger share of incoming requests.
- More complex to implement and does not consider current server load or response time.

**IP Hashing**

- Calculates a hash value from the client’s IP address to determine which server to route the request to.
- Used for session persistence, as requests from the same client are always directed to the same server.
- Can lead to uneven load distribution if certain IP addresses generate more traffic than others.
- Lacks flexibility if a server goes down, as the hash mapping may need to be reconfigured.

**Least Connections**

- Monitors the number of active connections on each server.
- Assigns incoming requests to the server with the least number of active connections.

**Least Response Time**

- Monitors the response time of each server.
- Assigns incoming requests to the server with the fastest response time.
- May not consider other factors such as server load or connection count.

### Disadvantages of Load Balancers

- A single load balancer is a single point of failure; configuring multiple load balancers increases complexity.
- The load balancer can become a performance bottleneck if it lacks resources or is not configured properly.

## Proxy Servers

![https://substackcdn.com/image/fetch/f_auto,q_auto:good,fl_progressive:steep/https%3A%2F%2Fsubstack-post-media.s3.amazonaws.com%2Fpublic%2Fimages%2F87424c89-0ba3-4580-89bb-ccd5870dff69_945x419.png](https://substackcdn.com/image/fetch/f_auto,q_auto:good,fl_progressive:steep/https%3A%2F%2Fsubstack-post-media.s3.amazonaws.com%2Fpublic%2Fimages%2F87424c89-0ba3-4580-89bb-ccd5870dff69_945x419.png)

### Proxy (Forward Proxy) Server

- A proxy (or forward proxy) is a server that acts on behalf of clients on a network.
- When you send a request (e.g., opening a webpage), the proxy intercepts it, forwards it to the target server, and relays the server’s response back to you.

#### How a Proxy Server Handles a Request

- The user types a website URL into their browser. The request is intercepted by the proxy server instead of going directly to the website.
- The proxy server examines the request to decide if it should forward, deny, or serve a cached copy.
- If the proxy forwards the request, it contacts the target website. The website sees only the proxy server’s IP, not the user’s.
- When the target website responds, the proxy receives the response and relays it to the user.

#### Benefits

- Privacy and Anonymity: Proxy servers hide your IP address so the destination server does not know your real location.
- Access Control
- Security: Proxies can filter out malicious content and block suspicious sites.
- Improved Performance: Caches frequently accessed content.

#### Real World Applications

- Bypassing Geographic Restrictions: For example, a server in the US gives data specific to US users, but you want the same data from India. You call a proxy server set up in the US, which calls the main server and returns the desired response.
- Speed and Performance Optimization (Caching): Uses caching for frequently accessed data and TTL to deal with stale data.

### Reverse Proxy

- A reverse proxy is a web server that centralizes internal services and provides unified interfaces to the public. Requests from clients are forwarded to a server that can fulfill them before the reverse proxy returns the server's response to the client.
- Think of a reverse proxy as a **gatekeeper**. Instead of hiding clients from the server, it hides servers from clients.
- Allowing direct access to servers can pose security risks like DDoS attacks.

#### How a Reverse Proxy Handles a Request

- The client makes a request to the reverse proxy server, which routes it to the appropriate backend server based on load balancing and availability.
- The backend server processes the request and sends the response back to the reverse proxy.
- The reverse proxy relays the response to the client.

#### Benefits

- Enhanced security: Reduces risk of attacks on backend servers as they are not exposed.
- Load Balancing
- Caching static content
- SSL Termination: Decrypts incoming requests and encrypts server responses so backend servers do not have to perform these expensive operations. Removes the need to install [X.509 certificates](https://en.wikipedia.org/wiki/X.509) on each server.
- Web Application Firewall (WAF): Can inspect incoming requests, acting as a firewall to detect and block malicious traffic.

#### Real World Applications

- **Cloudflare’s** reverse proxy is widely used by global websites and applications to boost speed, security, and reliability.
- Its **Web Application Firewall (WAF)** and **DDoS protection** block malicious traffic before it reaches the site’s servers, safeguarding against attacks and improving uptime.

#### Disadvantages

- Complexities
- A single reverse proxy can introduce a single point of failure.

### API Gateway

- An API Gateway is a specialized reverse proxy that acts as a single entry point into a system of microservices.
- It's not just a traffic router, it enforces security, policies, and orchestration logic.

#### Features

- Routing (based on paths, headers, etc.)
- Authentication & Authorization (JWT, OAuth2)
- Rate limiting & throttling
- Request/response transformation
- Logging, monitoring, tracing
- API versioning and quota management

### Clarification

- VPN and proxy are not the same. VPN encrypts all internet traffic, while a proxy only forwards specific requests.

## Load Balancer vs Reverse Proxy

- Deploying a load balancer is useful when you have multiple servers. Load balancers route traffic to a set of servers serving the same function.
- Reverse proxies can be useful even with just one web server or application server, providing the benefits described above.

## Key Differences

| Feature                             | Load Balancer      | Reverse Proxy     | API Gateway                |
| ----------------------------------- | ------------------ | ----------------- | -------------------------- |
| Purpose                             | Distribute traffic | Forward requests  | Manage and route API calls |
| OSI Layer                           | Layer 4 or 7       | Layer 7           | Layer 7                    |
| Authentication/AuthZ                | ❌ No              | ⚠️ Maybe          | ✅ Yes                     |
| Rate Limiting                       | ❌ No              | ⚠️ Maybe          | ✅ Yes                     |
| Request Transformation              | ❌ No              | ⚠️ Limited        | ✅ Yes                     |
| SSL Termination                     | ✅ Yes             | ✅ Yes            | ✅ Yes                     |
| Awareness of Microservices internal | ❌ No              | ❌ No             | ✅ Yes                     |
| Use Case                            | Scalability        | Security, routing | Microservice orchestration |

## Where sits in real world

[![](https://mermaid.ink/img/pako:eNp9klFv2jAQx7-K5ScmpSixA6TRVInEaJrUamhsL0v24MY3iEZs5DhtM8R3n7Fh0EzCT_nd3f9_54v3uFICcIrXmu826BsrJbKn7Z59IN_WIA1a1QJ85njmxfcW9Cn308dBilIOxAuxBvTIe9AXbVY8Ki5QxrdcVqBvqD9xA6-8HxrkxXz5-Zy8IX-qK63smC91Be3QhEX-CiufRx-f9QN6rc0GfYUX0C2gpVZvZ3unIMUXLS6So-J_AbpW0GLJ-8at75bmxh0YN3w4-iIqRm52ln246rYgxcgPOIjTYnQe45K5ajZHd3cPKPOQOcg95A5Y9I7IO6KeWORwcSplxOOpllGPFAe4Ad3wWtgHtz8mS2w20ECJU_spuP5d4lIebB3vjFr1ssKp0R0EWKtuvcHpL75tLXU7Yf8_q7ldU_MvCqI2Sj_59-yedYB3XP5QqjnbrPWx98nSLgF0rjppcBqRxBXjdI_fLNJwTEiYTMgknsZJREmAe5zOZuNwmtjaiFA6IzE9BPiPsw_HFqfRJLyfhfEkuU_iw1-k7Py7?type=png)](https://mermaid.live/edit#pako:eNp9klFv2jAQx7-K5ScmpSixA6TRVInEaJrUamhsL0v24MY3iEZs5DhtM8R3n7Fh0EzCT_nd3f9_54v3uFICcIrXmu826BsrJbKn7Z59IN_WIA1a1QJ85njmxfcW9Cn308dBilIOxAuxBvTIe9AXbVY8Ki5QxrdcVqBvqD9xA6-8HxrkxXz5-Zy8IX-qK63smC91Be3QhEX-CiufRx-f9QN6rc0GfYUX0C2gpVZvZ3unIMUXLS6So-J_AbpW0GLJ-8at75bmxh0YN3w4-iIqRm52ln246rYgxcgPOIjTYnQe45K5ajZHd3cPKPOQOcg95A5Y9I7IO6KeWORwcSplxOOpllGPFAe4Ad3wWtgHtz8mS2w20ECJU_spuP5d4lIebB3vjFr1ssKp0R0EWKtuvcHpL75tLXU7Yf8_q7ldU_MvCqI2Sj_59-yedYB3XP5QqjnbrPWx98nSLgF0rjppcBqRxBXjdI_fLNJwTEiYTMgknsZJREmAe5zOZuNwmtjaiFA6IzE9BPiPsw_HFqfRJLyfhfEkuU_iw1-k7Py7)

## Pending Topics

- Nginx architecture
- HAProxy architecture
