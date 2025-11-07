# DNS

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
