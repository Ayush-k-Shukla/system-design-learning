# Authentication System

## Requirements

### Functional

- Signup
- login
- logout
- session management (long lived sessions)
- Will support single device login first
- only support username based auth no (Oauth and google login)

### Non Functional

- Secure
- Scalable to Millions of users
- Availability

## Estimation

- DAU -> 1 Million

### Traffic

- QPS -> 1 Million / 24 hr = 12
- But we will see that most traffic comes in peak hour, so if peak hour is 4Hr then
- Peak QPS -> 75 QPS
- We will design to handle the 100+ QPS

### Storage

- 1 user store
  - userid : 16 byte
  - email/username: 50 bytes
  - hashes pass: 60 bytes
  - other metadat: 100 bytes
- So in total 200 bytes
- Full - 1 million x 200 = 200 MB
- On top session storage can also take like 1 token (512 bytes) \* 1 M = 512 MB

## High level design

<img src="/img/hld-questions/auth/hld.png" />

### API gateway / Load balancer

- Mainly used to rate limit, route request to correct pod

### Auth Service

- Handles login of the user
- Singup of the user
- Encrypt (using bcrpt or crpto with good salt) the password and store it in DB
- Logout as well (remove session)
- Allows only unique emails/usernames

### Database (SQL)

- Stores user data like email, username and password
- Auth service call it to verify the entered password during login

### Redis DB

- This is used to store the sessions which are active
- redis is chosen over normal DB as it has auto expiry TTL option so no need to manually delete Session after expiry and also redis is fast
- TTL can be inactivity bases (LRU)

## DB Design

<img src="/img/hld-questions/auth/db.png" />

## API Design

### POST /signup

- pass email and password
- if email not present create and entry in DB, store hash encrpted password

### POST /login

- pass email and password
- if validated
  - create a session entry in redis
  - return sessiontoken back to client
- client will store returned token in local storage and send in subsequent request to be validated

### POST /logout

- accepts session token in auth headers
- if validated token then remove token from redis and logout user
- after token removal if user call api again service will return 401

### GET /me

- get logged in user details
- accepts session token in auth headers

## Security

- No **CSRF** issue as we will not be passing token in cookies, token will be stored in local storage client side
- Session token can not be guesses as it is generated using UUID
- Use HTTPS so data can not be read
- Login endpoint can be called brute force to fix this
  - we already have rate limiting on the API gateway
  - we will block a account for certain period if suspicious calls made
- Token leakage not possible as token will be passed in auth headers
- XSS will be handled on frontend side

## Scalability

- As our usecase is not much write heavy so we need read heavy read replicas
- Rate limit based on IP

## Failure Handeling

- If redis fails what to do
  - if redis fails we can retry
  - another extra level solution can be, we make a replica of redis like data in DB as well and fallback to DB in case of redis failure
- If DB fails
  - return clear 503 error

## Availability

For high availability we can do lot of things

- run multiple instance of auth service (Horizontal scaling)
- have DB backup and replica (master - slave)
- Each service is stateless so can expand without issue and dependency
