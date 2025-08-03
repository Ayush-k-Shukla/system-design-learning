# Authentication Types

- Here are some major used authentication mechanism used

## Password Based

- User provides username and password, server validate against it.

### Pros

- easy to implement
- no 3rd party depndency

### Cons

- Security prone to bruteforce attacks, Password leakage
- Need extra flow like
  - forgot password
  - reset password

### Usecase

- Very early stage startup
- Internal tool with limited users

---

## Token Based

- Client send credentials once and server return back a token on validation
- Next time client access protected resource with help of token
- Mostly the token is **JWT**.
- While designing focus on short lived JWT tokens with refresh token logic.
- Avoid putting sensitive data in token, also use signed JWT tokens.

### Pros

- stateless so easy to scale horizontally
- Some metadata can be added in th token itself like roles, expiry etc.

### Cons

- Overhead in request call need to pass everytime as stateless
- Hard to revoke JWT token untill expiry (need extra logic handeling like blacklist maintain)
- token is large so extra request size everytime

### Usecase

- public APIs
- Mobile apps and SPAs - as cookies hard to manage on mobile
- Microservice env (Distributed system)

---

## Session Based

- After login server create a session and stores it in Redis or DB.
- Clients gets a session Id via cookies or from response.
- If giving sessionId in cookies, then should set samSite only to avoid csrf.

### Pros

- Easy to invalidate as just need to delete session entry in DB
- Secure and remember state

### Cons

- Doesn't scale good as has dependency on session store
- Extra server side manage logic

### Usecase

- Monolith app
- traditional ssr apps

---

## Email/OTP Based (Magic Link or Code)

- User enters email or phone number and receives a **one-time code or login link**.
- Once verified, they are considered authenticated.

### Pros

- No password required (better UX and security).
- Immune to password leaks or reuse attacks.

### Cons

- OTP/email delivery can be delayed or fail.
- Requires integration with reliable email/SMS providers.
- Can become annoying if asked repeatedly.

### Usecase

- Guest login or temporary access.
- Apps with users unfamiliar with passwords (e.g. fintech, rural apps).
- Low-friction signup flows.

---

## Multi-Factor Authentication (MFA / 2FA)

- Requires **two or more** of the following:
  - Something you know (password)
  - Something you have (OTP device, phone)
  - Something you are (biometrics)

### Pros

- Significantly more secure.
- Reduces risk of credential-stuffing or phishing.

### Cons

- Slightly worse UX.
- Requires user setup and recovery flows.
- Can cause lockouts if second factor is lost.

### Usecase

- Banking or payment systems.
- Admin access to dashboards.
- Enterprise SaaS with sensitive data.

---

## OAuth 2.0 / OpenID Connect

- Users authenticate via **trusted identity providers** (Google, GitHub, Facebook, etc).
- Your system gets limited access via **access tokens**.

### Pros

- No need to manage passwords.
- Simplifies login and signup.
- Trusted third-party security.

### Cons

- Protocol is complex (redirects, scopes, grants).
- External dependency (provider must be online).
- Some providers enforce rate limits or app review.

### Usecase

- Social login (e.g., "Login with Google").
- Single Sign-On (SSO) for internal company tools.
- When you need delegated access to user data (e.g. Google Drive).

---

## API Key Based

- A long, random **secret key** is passed in headers or query string to authenticate API requests.

### Pros

- Simple to implement and use.
- Good for service-to-service communication.

### Cons

- Cannot tie directly to user identity.
- Difficult to revoke or rotate at scale.
- Keys can be accidentally exposed.

### Usecase

- Internal APIs and microservices.
- External APIs for partners or developers.
- CLI tools or automation bots.

---

## Certificate Based Authentication (Mutual TLS)

- Clients and servers authenticate each other using **digital certificates** (X.509).
- Commonly used with **mutual TLS**.

### Pros

- Extremely secure for machine-to-machine communication.
- Certificates are hard to spoof.

### Cons

- Hard to manage certificate lifecycle.
- Complex to configure and rotate certs.

### Usecase

- Backend microservices in Kubernetes or service mesh.
- B2B enterprise systems.
- Internet of Things (IoT) devices.

## More Details around the Digital certificate working and Mutual TLS

A **digital certificate** is like an online **identity card**, issued by a **Certificate Authority (CA)**. It helps verify the identity of a server or client during encrypted communication (like HTTPS or mTLS).

### 📄 A digital certificate contains:

- Public key of the owner (server/client)
- Identity details (e.g., domain, organization)
- Expiry date
- CA’s digital signature

### ✅ Why are they used?

- To **secure communication** over the internet (e.g., HTTPS).
- To **verify identity** and avoid impersonation or man-in-the-middle attacks.

---

## What is Mutual TLS (mTLS)?

### 🔁 Regular TLS (used in HTTPS)

- **Client verifies the server** via its digital certificate.
- Server doesn’t care who the client is — anyone can access public pages.

### 🔐 Mutual TLS (mTLS)

- **Both client and server verify each other's certificates**.
- Enables **two-way authentication**.

> This adds an extra layer of trust and is commonly used in internal and secure systems.

---

## 🔍 Analogy

- **Digital Certificate** → Your government ID.
- **TLS** → You verify a website's ID (e.g., https://google.com).
- **mTLS** → Both parties show ID to each other before starting a conversation.

---

## ✅ Pros of mTLS

- 🔐 Strong authentication (both parties verified)
- 🔒 End-to-end encrypted channel
- 🛡️ Resistant to impersonation and MITM attacks

## ❌ Cons of mTLS

- ⚙️ More complex setup
- 🔁 Requires certificate rotation and management
- ❌ Not ideal for public user authentication (too heavy)

---

## 🛠️ Use Cases for mTLS

- 🔄 Internal microservice communication (e.g., in Kubernetes)
- ☁️ IoT devices authenticating with cloud services
- 💰 Financial systems
- 🧪 Service mesh (e.g., Istio, Linkerd)
- 🚀 B2B secure API integrations

---

## 🔄 TLS vs Mutual TLS (mTLS)

| Feature                   | TLS (Standard HTTPS)      | Mutual TLS (mTLS)                 |
| ------------------------- | ------------------------- | --------------------------------- |
| Who presents certificate? | Server only               | Both client and server            |
| Who verifies identity?    | Client only               | Both client and server            |
| Client identity verified? | ❌                        | ✅                                |
| Use case                  | Public websites, browsers | Internal APIs, service-to-service |
| Setup complexity          | Low                       | High                              |
| Security level            | Good                      | Very Strong                       |

---

## 🔐 Summary

- Digital certificates prove **identity** using public/private key encryption.
- TLS ensures secure communication from **client → server**.
- Mutual TLS ensures secure communication in **both directions**, ideal for **secure internal communication**.
