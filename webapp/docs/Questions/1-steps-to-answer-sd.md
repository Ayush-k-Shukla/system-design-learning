# How to Answer

<p align="center">
    <img src="/img/hld-questions/steps.jpg"/>
</p>

## Requirement Gathering

1. **Functional**
   1. Core features
   2. Critical features
   3. Users of system (internal, cust, etc.)
   4. How will user interact (API, web, mobile...)
   5. What actions user allowed to perform
2. **Non-Functional**
   1. Scale of system in term of users and requests
   2. Data volume we expect
   3. read to write ratio
   4. Availability of how many 9's

## Capacity estimation

It wont be needed in depth in each so ask intervieewer beforehand.

1. Daily users count we can expect
2. Traffic (peak read/write scenarios)
3. Storage (Type of data to be stored) and estimation of total amount of storage required
4. Memory (will caching we helpful)

## High Level design

1. break down into smaller pieces like LB, FE, BE, Cache, DB, Queues etc.

## DB design

1. Choose between SQL vs NOsql based on Data modeling needed.
2. Design DB schema
3. Define Data access patterns, and add index and caching based on that

## API Design

1. Indentify the API requirements, based on the functionalities,
2. Choose api style (REST, Graphql, gRPC) based on client need
3. Define Endpoints
4. Specify data formats (JSON, xml)

## Deep dive into Key components

1. Deep dive into high level of business logic like each service , what will it do
2. Also explain LB, caching, Authentication etc.

## Address Key issues

1. Address Scalability, performance concerns
2. Reliability
