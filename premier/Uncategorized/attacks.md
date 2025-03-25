# Attacks

---

## Network based Attacks

---

### Denial of a Service (DoS)

- A DoS attack overwhelms a server, network, or system with excessive requests, making it unavailable to legitimate users. Unlike DDoS, DoS is typically performed from a single source.
- **Scenario**

  - Sending thousands of request per second to login page, causing resource exhaustion.
  - exploiting a compute heavy API call to overload the backend.

- ## **Prevention Strategies**

  - **CAPTCHA:** Prevents automated scripts from bombarding your service
  - **Rate Limiting:** Use tools like NGINX, Cloudflare, or AWS WAF to limit request rates per user/IP.
  - **Throttling & Queuing:** Implement exponential backoff and queue requests.

### Distributed Denial of a Service (DDoS)

- A DDoS attack uses multiple compromised machines (botnets) to flood a system with traffic. Unline DoS it uses multiple compromised systems (forming botnets).
- **Prevention**
  - Use a CDN to offload traffic to edge servers
  - Use Anycast Network Routing to ditribute traffic geographically.
  - Use WAF(web app firewall) to filter traffic

### Main-in-the-Middle (MitM)

- An attacker intercepts communication between two parties, potentially modifying or stealing data.
- **Scenarios**
  - On public wifi intercept http traffic
- **Prevention**
  - Use HTTPS
  - Encrypt sensitive data before transmission
  - Add TLS and SSL certificates

### DNS Spoofing

- An attacker poisons a DNS resolver to redirect traffic to a malicious site.
- **Scenarios**
  - A user enters example.com, but due to a poisoned DNS cache, they are redirected to a fake example.com for phishing.
- **Prevention**
  - Use Secure Recursive DNS like (Google public DNS, Cloudfare DNS)
  - Reduce TTL of DNS cache records

---

## Web App Attacks

---

### SQL Injection

- SQL Injection occurs when an attacker manipulates a web application's SQL queries by injecting malicious SQL code. This can allow them to retrieve, modify, or delete database data.
- **Example**

  - User login Query

    ```sh
       SELECT * FROM users WHERE username = 'admin' AND password = 'password';
    ```

  - If an attacker inputs admin' -- as the username, the query becomes:

    ```sh
       SELECT * FROM users WHERE username = 'admin' -- ' AND password = 'password';
    ```

  - And will bypass authentication and give all data

- **Prevention**

  - Use Parametrized Queries which ensures that user input are treated as input no executable sql.
    ```sh
       await connection.execute(
       "SELECT \* FROM users WHERE username = ? AND password = ?",
       [username, password]
       );
    ```
  - Use ORMs as they will not allow direct SQL manipulation
  - Validate and Sanitize user input
  - Never use the root user for database queries. Create a dedicated user with minimum required permissions
  - Dont expose DB error to client

### Cross Site Scripting (XSS)

- It is a client side attack where an attacker injects malicious scripts into web pages(usually js).
- **Types**

  - **Stored XSS**

    - Malicious scipt is stored in the DB and it will execute everytime page load, affecting all users.
    - **Example:** Attackers injects `<script>alert('hacked')</script>` into a comment field.

  - **Reflected XSS**

    - The malicious script is sent in a request (e.g., via URL or form input) but not stored on the server.
    - **Example:**
      - Attacker sends a phishing link `https://example.com/search?q=<script>stealCookies()</script>`
      - If website directly reflect the param input without sanitization, the script executes in the victim's browser.

  - **DOM based XSS**

    - Happens entirely on the client-side (browser) by manipulating the DOM
    - **Example:**
    - `document.getElementById("output").innerHTML = location.search;`
    - The script will execute because `innerHTML` is used unsafely.

  - **Prevention**

    - **Escape User Input (Encode output)**

      - Convert special characters into safe equivalents `(<, >, &, etc.)`.
      - Instead of directly rendering use escaped version.

        ```
                    function escapeHTML(str) {
                    return str.replace(/&/g, "&&")
                    .replace(//g, ">>")
                    .replace(/"/g, """")
                    .replace(/'/g, "''");
                    }


                    use : <div>Welcome, {{ escapeHTML(username) }}</div>
                    insteadof: <div>Welcome, {{username}}</div>
        ```

      - React, Vue, and Angular escape HTML by default. But dangerous APIs like innerHTML still allow XSS.

    - **Use Content Security Policy(CSP)**

      - CSP blocks execution of untrusted scripts even if injected.
      - Blocks inline scripts and external scripts unless explicitly allowed.
      - set csp header to default-src 'self'

    - **Use Secure JS functions**

      - Avoid `innerHTML`, `document.write`, `eval`
      - Instead use `textContent` or `createElement`

    - Use `httpOnly` flag for cookies to prevent JS access
    - Use secure lib like `DOMPurify` for FE and `helmet` for BE

### Cross Site Request Forgery (CSRF)

### Remote Code Execution

### Path Traversal Attack

---

## Authentication and Authorization Attacks

---

### Brute Force Attack

### Session Hijacking

### OAuth and JWT Atacks
