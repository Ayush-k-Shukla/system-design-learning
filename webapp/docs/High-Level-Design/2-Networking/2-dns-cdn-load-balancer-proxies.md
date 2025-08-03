# DNS, CDN, Load balancer, Proxies

## Domain Name System

- It maps a domain name to IP address.

![image.png](/img/hld/image.png)
![image.png](/img/hld/how-does-dns-resolution-work.webp)

### Steps to Check

- The browser (client) checks if the hostname to IP address mapping exists in the local cache of the client.
- If the last step failed, the client checks the Operating System (OS) local cache by executing a system call (syscall).
- If the last step failed, the client makes a DNS request to the Gateway/Router and checks the local cache of the Router.
- If the last step failed, the router forwards the request to Internet Service Provider (ISP) and checks the DNS cache of the ISP.
- If the last step fails, the DNS resolver queries the root servers (there are 13 root servers with replicas worldwide).
- DNS resolver queries Top Level Domain (TLD) servers such as .com, or .org. DNS resolver queries Authoritative name servers such as google.com.
- Optionally, the DNS resolver queries Authoritative subdomain servers such as maps.google.com depending on your query.

## Content Delivery Network (CDN)

- It is a globally distributed network of connected servers , serving data from location near to user.
- Generally it is used for serving static data like images, html, css, js but some CDN’s also support dynamic content.

### Types

#### Pull

- It like you added a new post with static data to your app, and some different location person tries to access in that case first time the cdn will take time to load the data as it is not present there. so means when user access a data which is not on cdn it takes a pull and store it nearby.

#### Push

- instead of waiting around for the CDN to pull the content when it’s needed, you simply upload the entire content of your travel blog to the CDN beforehand. That way your pictures, theme files, videos, and the rest are always on the CDN servers around the world.

- Generally setting up a **pull CDN** is easy. Once it is initially configured a pull CDN seamlessly stores and updates content on it’s server as requested. The data generally stays there for 24 hours if not modified .

  - However, what makes a pull CDN easy to use can also be a drawback. When making changes to a blog, you typically don't have control over how long the pull CDN cache lasts. If you update an image, it might take up to 24 hours to reflect the changes, unless you shut off the CDN or clear its cache.

- The decision on which CDN type to go with revolves in large part around traffic and downloads. Travel blogs that are hosting videos and podcasts (aka. large downloads) will find a push CDN cheaper and more efficient in the long run since the CDN won’t re-download content until you actively push it to the CDN. A pull CDN can help high-traffic-small-download sites by keeping the most popular content on CDN servers. Subsequent updates (or “pulls”) for content aren’t frequent enough to drive up costs past that of a push CDN.

### Benefits

- The request fulfilled by your CDN will not go to server
- less distance travelled means faster response time

### Disadvantages

- Content might be stale if it is updated before the TTL expires it.
- CDN costs could be significant depending on traffic, although this should be weighed with additional costs you would incur not using a CDN.

## Load Balancer

- It distributes incoming client requests to computing resources such as application servers and databases.
- These are effective at
  - Preventing overloading resources
  - Preventing requests from going to unhealthy servers
  - Helping eliminate single point of failure

### Types

#### Hardware Load balancer

- Physical devices designed for high-speed traffic distribution.
- Expensive and used in enterprise setup
- e.g. Citrix ADC

#### Software Load Balancers

- Applications or services that run on standard servers
- Cost effective and flexible
- e.g. HAProxy, Nginx

#### Cloud Load Balancers

- Managed service provided by cloud providers
- Scalable and easy to integrate
- e.g. Google Cloud LB, Amazon Elastic LB

### Types based on functionalities

1.  **Layer 4 (Transport layer) LB**
    1. Operates at the TCP/UDP layer.
    2. Routes traffic based on IP addresses and ports.
    3. Example: AWS Network Load Balancer (NLB).
2.  **Layer 7 (Application layer) LB**
    1. Operates at the HTTP/HTTPS layer.
    2. Makes routing decisions based on URL, headers, cookies, etc.
    3. Example: AWS Application Load Balancer (ALB).

### **Types of Load Balancing Algorithms**

1.  **Round Robin**
    1. A request is sent to the first server in the list.
    2. The next request is sent to the second server, and so on.
    3. After the last server in the list, the algorithm loops back to the first server.
    4. Preferred only when all servers have same processing capabilities, will cause issue if some servers has diff processing capabilities.
2.  **weighted round robin**
    1. Each server is assigned a weight based on their processing power or available resources.
    2. Servers with higher weights receive a proportionally larger share of incoming requests.
    3. More complex to implement and does not consider server current load or response time.
3.  **IP hashing**
    1. Calculates a hash value from the client’s IP address and uses it to determine the server to route the request.
    2. Used when we need session persistence, as request from same client are always directed to same server
    3. Can lead to uneven load distribution if certain IP addresses generate more traffic than others.
    4. Lacks flexibility if a server goes down, as the hash mapping may need to be reconfigured.
4.  **Least connections**
    1. Monitor the number of active connections on each server.
    2. Assigns incoming requests to the server with the least number of active connections.
5.  **Least response time**
    1. Monitors the response time of each server
    2. Assigns incoming requests to the server with the fastest response time
    3. May not consider other factors such as server load or connection count.

### **Disadvantage**

1.  A single load balancer is a single point of failure, configuring multiple load balancers further increases complexity.
2.  The load balancer can become a performance bottleneck if it does not have enough resources or if it is not configured properly.

## Proxy Servers

![https://substackcdn.com/image/fetch/f_auto,q_auto:good,fl_progressive:steep/https%3A%2F%2Fsubstack-post-media.s3.amazonaws.com%2Fpublic%2Fimages%2F87424c89-0ba3-4580-89bb-ccd5870dff69_945x419.png](https://substackcdn.com/image/fetch/f_auto,q_auto:good,fl_progressive:steep/https%3A%2F%2Fsubstack-post-media.s3.amazonaws.com%2Fpublic%2Fimages%2F87424c89-0ba3-4580-89bb-ccd5870dff69_945x419.png)

1. **Proxy (forward proxy) Server**
   1. In computer terms, a proxy (or a forward proxy) is a server that acts on behalf of clients on a network.
   2. When you send a request, like opening a webpage, the proxy intercepts it, forwards it to the target server, and then relays the server’s response back to you.
   3. **How a proxy server handles a request**
      1. The user types a website URL into their browser. The request is intercepted by the proxy server instead of going directly to the website.
      2. The proxy server examines the request to decide if it should forward it, deny it, or serve a cached copy.
      3. If the proxy decides to forward the request, it contacts the target website. The website sees only the proxy server’s IP, not the user’s.
      4. When the target website responds, the proxy receives the response and relays it to the user.
   4. **Benefits**
      1. Privacy and Anonymity
         1. proxy server hide our IP address so the destination server don’t our real location
      2. Access Control
      3. Security
         1. proxies can filter out malicious content and block suspicious sites
      4. Improved Performance
         1. It also caches frequently accessed content
   5. **Real world applications**
      1. Bypassing Geographic Restrictions
         1. Like there is a server in US which give data specific to user in US but you also want same data from india, in that case you will call proxy server setup in US and it will call main server, which will give desired response.
      2. Speed and Performance Optimization (Caching)
         1. with use of caching for most accessed data. it also uses TTL to deal with stale data
2. **Reverse Proxy**
   1. A reverse proxy is a web server that centralizes internal services and provides unified interfaces to the public. Requests from clients are forwarded to a server that can fulfill it before the reverse proxy returns the server's response to the client.
   2. Think of a reverse proxy as a **gatekeeper**. Instead of hiding clients from the server, it hides servers from clients.
   3. Allowing direct access to servers can pose security risk like DDoS attack.
   4. **How handles request**
      1. Client make request to reverse poxy server and this reverse proxy server then route it to appropriate backend server based on load balancing and availability
      2. BE server process the request and sends the request back to reverse proxy
      3. reverse proxy relays the response to client
   5. **Benefits**
      1. Enhanced security - reduce risk of attacks on BE servers as not exposed
      2. Load Balancing
      3. Caching static content
      4. **SSL termination**
         1. Decrypt incoming requests and encrypt server responses so backend servers do not have to perform these potentially expensive operations
         2. Removes the need to install [X.509 certificates](https://en.wikipedia.org/wiki/X.509) on each server
      5. **Web application Firewall (WAF)**
         1. it can inspect incoming requests, acting as a firewall to detect and block malicious traffic
   6. **Real world application**
      1. **Cloudflare’s** reverse proxy is widely used by global websites and applications to boost speed, security, and reliability.
      2. It’s **Web Application Firewall (WAF)** and **DDoS protection** blocks malicious traffic before it reaches the site’s servers, safeguarding against attacks and improving uptime.
   7. **Disadvantage**
      1. complexities
      2. A single reverse proxy can introduce single point of failure
3. **Some clarification**
   1. VPN and proxy are not same as VPN encrypts all out internet traffic, and proxy only forwards specific request

### Load Balancer vs Reverse Proxy

1. Deploying a load balancer is useful when you have multiple servers. Often, load balancers route traffic to a set of servers serving the same function.
2. Reverse proxies can be useful even with just one web server or application server, opening up the benefits described in the previous section.

Pending things

1. Nginx architecture
2. HAProxy architecture
