# Attacks

## Network based Attacks

### Denial of a Service (DoS)

- A DoS attack overwhelms a server, network, or system with excessive requests, making it unavailable to legitimate users. Unlike DDoS, DoS is typically performed from a single source.
- **Scenario**

  - Sending thousands of request per second to login page, causing resource exhaustion.
  - exploiting a compute heavy API call to overload the backend.

- **Prevention Strategies**

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

## Web App Attacks

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

- It is an attack where an unauthorized request is sent from a user who is authenticated in a web app.
- CSRF exploits the trust that a web application has in the user's browser, leveraging session cookies and stored authentication tokens.
- **Working**

  - victims logs into a website(e.g. bank) and receives an authentication cookie.
  - now somehow attacker tricks victim to goto `evil.com` and from there attacker makes a reques to `bank` api.
  - and as user is already authenticated so browser will automatically embed the auth cookies.
  - The server processes the request because it trusts the authenticated user's cookies.

- **Prevention**

  - Just to mention CORS shows error on browser but still send request to server so will not help.
  - **CSRF tokens**
    - It is a random unique value generated by server and embedded into forms or headers.
    - CSRF token are sent in form or headers as browser will no auto embed in every request.
    - When client make the request it has to send csrf token in request. (localstorage store it)
  - **Use `SameSite` Cookie Attriute**
    - By setting cookies as `SameSite=strict` or `SameSite=lax`, browsers will prevent cookies from being sent with cross-site requests.
  - Verify `Origin` and `Referrer` Headers
    - limitation as some browser(like mobile apps) may not always include these headers
    - Origin Header → Should match the site's domain.
    - Referer Header → Should match the current URL.
  - For Critical Actions (money transfer) ask user to reauth or MFA

### Remote Code Execution

- RCE happens when an attacker executes arbitrary code on the server due to improper input handling.
- **Example**

  ```sh
    app.get('/exec', (req, res) => {
      exec(req.query.command, (err, stdout) => {
        res.send(stdout);
      });
    });
  ```

  - If an attacker sends:

  ```
    http://example.com/exec?command=rm -rf /
  ```

  - This could delete files on the server.

- **Prevention**

  - Avoid using `exec` and `eval`
  - Run service with least previlages

### Path Traversal Attack

- Path Traversal exploits directory traversal vulnerabilities to access files outside the intended directory.
- **Example**

  ```sh
    app.get('/download', (req, res) => {
      const filePath = __dirname + "/uploads/" + req.query.file;
      res.sendFile(filePath);
    });
  ```

  - If an attacker sends:

  ```
    http://example.com/download?file=../../etc/passwd
  ```

  - If not properly handled, this reveals sensitive files.

- **Prevention**

  - Use Absolute Paths & Restrict Directory Access

    ```sh

      app.get('/download', (req, res) => {
        const safePath = path.join(\_\_dirname, 'uploads', path.basename(req.query.file));
        res.sendFile(safePath);
      });
    ```

  - Only allow specific files for download

## Authentication and Authorization Attacks

### Brute Force Attack

- It involves attackers using a dictionary or common used pwd or leaked creds and use authomated script to call.
- **Prevention**
  - Rate limiting
  - Captcha
  - Temporarily lock account after multiple failed attempts
  - MFA

### Session Hijacking

- Session hijacking occurs when an attacker steals a valid session ID to impersonate a legitimate user. This allows them to access the system as the victim without knowing their password.
- **Prevention**
  - Use secure and HttpOnly Cookies
  - Implement session expiry

### OAuth and JWT Attacks

- There can multiple way
  - Stolen JWTs token can be used for prolonged access.
  - Trick users into authorizing malicious apps.
  - If the secret key is weak or exposed, attackers can forge JWTs.
- **Prevention**
  - Use strong JWT signing Algo
  - Implement token expiry and rotation
  - Allow only whitelisted callback URLs for OAuth redirect Urls
  - Detect and remove compromised tokens (maintain blacklist in redis)
