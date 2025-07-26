# Distributed Unique Id genrator

## Applications

In most system where Requirements is to get unique id in a distributed env like `Url Shortener`, `Bank`, `MySQL or any other DB`.

## Requirements

### Functional

1. each time a service is called it should return a unique ID across all node in the system
2. Id should be fixed size, let's say 64 bit
3. Id should contain a creation timestamp

### Non-Functional

1. Highly consistent, means no duplicate key generation
2. Able to handle hight throughput

## Solution

### Single Machine(node)

For single machine we can use multiple approach like

1. Auto increment id
2. Auto increment id with creation timestamp of the system

### Random Numbers(Multi node)

This appraoch can work whenw we have to select ids from long range.

#### Universally Unique Identifier(UUID)

`550e8400-e29b-11d4-a716-446655440000`

1. This a uniqueid generated from a set of 2^128 ids available, It is 128 bits.
2. Based on the version of it the generation of id is diff
   1. **V1**
      1. use current timestamp and device's MAC address
   2. **V3**
      1. use MD5 hash logic
   3. **V4**
      1. use total random numbers
      2. most used
   4. **V5**
      1. uses SHA-1
3. **Collision Probability**
   1. Due to v4 version ensure 122 bits reserved for unique numbers so prob of collision is very low
   2. After generating trillions of urls the prob is still less than 0.5
4. **Advantages**
   1. collision is very rare
   2. non predictable as use random numbers (specially used in sensitive transactions)
5. **Disadv**
   1. as uses 128-bit so take extra storage space
   2. Can not use sorting

#### Twitter snowflake

<p align="center">
    <img src="/img/hld-questions/id-generater/snowflake.webp"/>
</p>

1. It is one of the most popular algorithms to generate unique ids in a distributed system.
2. It is in half of the size of the UUID.

3. Some properties of the Snowflake id is
   1. Ids are unique and sortable based on timestamp
   2. Ids include timestamp
   3. is a 64-bit
   4. only numerical values
4. **Structure**
   | Bits | Field | Description |
   |------|-------------|--------------------------------------------------|
   | 1 | Sign bit | Always `0` (ensures positive number). |
   | 41 | Timestamp | Milliseconds since a custom epoch (ensures order). |
   | 10 | Machine ID | Identifies the machine or data center (prevents conflict). |
   | 12 | Sequence | Increments per millisecond to prevent collisions (0-4095). |
5. **Breakdown**
   1. **Timestamp (41 bits):** Allows for 69 years of usage (2^41 milliseconds).
   2. **Machine ID (10 bits):** Supports up to 1024 machines generating IDs.
   3. **Sequence (12 bits):** Can generate 4096 unique IDs per millisecond per machine.
