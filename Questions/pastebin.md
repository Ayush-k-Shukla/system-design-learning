# Pastebin

## Requirements

### Functional

1. User should be able to paste data upto 1MB size
2. Customized urls
3. Expiration (Optional to ask to user or a preset defined)
4. Support text based data only
5. User is able to set visibility of a url to public or private
6. User can delete the paste as well if created by him only

### Non functional

1. High availability
2. Low latency
3. Durability

## Estimation

1. Per day write - 1M
2. read/write ratio - 10:1
3. Avg paste size - 100 KB
4. Max paste size - 1 MB

### Traffic

| Desc           | value                                 |
| -------------- | ------------------------------------- |
| QPS(write)     | (100k)/(sec in one day) = 10 (approx) |
| QPS(read)      | 10\*10 = 100                          |
| Peak QPS(read) | 40 (assumption)                       |

### Storage

| Desc               | value                   |
| ------------------ | ----------------------- |
| storage per day    | 1MBx1M = 1 TB           |
| storage for 5 year | 1 GB x 365 x 5 = 1.8 PB |

### Bandwidth

| Desc    | value                                                  |
| ------- | ------------------------------------------------------ |
| Ingress | 1MB/paste x1M paste/day x 10^(-5)day/sec = 10 MB/sec   |
| Egress  | 1MB/paste x10M reads/day x 10^(-5)day/sec = 100 MB/sec |

### Memory(Caching)

We can follow the 80/20 rule while caching where 80% of the trafic is server by 20% cached result and rest can be done from server. We can use cache with a TTL of 1 day.

| Desc       | value                                 |
| ---------- | ------------------------------------- |
| cache size | 1MB/paste x10M paste/day x 0.2 = 2 TB |

## API Design

We can use REST for ease of loose coupling and easiness to debug.

### Create paste

Request

```sh
/pastes
method: POST
authorization:...
{
   name:<string>,
   content:<string>,
   visibility:<enum>,
   custom:<string>, (optional)
   expiry:<dattime> (optional)
}
```

Response

```sh
201
{paste-id-url}

401 - unauth
```

### Get paste

Request

```sh
/pastes/:paste-id
method: GET
authorization:...
```

Response

```sh
200
{
   name:<string>,
   content:<string>,
   visibility:<enum>,
   id:<string>, (optional)
   expiry:<dattime> (optional),
   s3_link:<string> (optional) - if paste size is more than 100KB we will store some part of it to s3
}


401 - unauth
```

### Delete paste

```sh
/pastes/:paste-id
method: DELETE
authorization:...
```

## Database

1. As there it is going to be a lot of data so we need to make decision which DB we want to use. I am planning to choose SQL because of this point
   1. Strict schema
   2. relational data
   3. need of complex joins
   4. lookup by index
2. Also there is size very huge so we can store huge content on s3 as it will cost effectively and reduce DB io.

<p align="center">
   <img src="./images/pastebin/schema-design.svg"/>
</p>
