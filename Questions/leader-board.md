# Leaderboard

## Requirements

### Functional

1. User should be able to see top 10 players based on score
2. Can see specific player's rank as well
3. Can see surrounding players rank if search a specific player
4. leaderboard can rank player's based on daily, monthly (time filter)
5. Can see historic match leaderboard as well
6. Leaderboard can be updated in full distributed manner
7. Should support thousand of players
8. Need realtime

### Non functional

1. High availability
2. Low latency
3. Scalability
4. Reliability

## Estimation

1. Users are globally distributed
2. read/write ratio - 5:1
3. Daily active users for writes - 50 Million
4. Player id can be 30 bytes string and score can be 16 bit int consuming 2 bytes.

### Traffic

| Desc           | value                                     |
| -------------- | ----------------------------------------- |
| DAU (write)    | 50 M                                      |
| QPS(write)     | (50M)/(sec in one day) = 587 (600 approx) |
| QPS(read)      | 600\*5 = 3000                             |
| Peak QPS(read) | 4000 (assumption)                         |

### Memory (for fater use)

| Desc                         | value             |
| ---------------------------- | ----------------- |
| total player                 | 50 M              |
| Single player record         | (30+2) = 32 bytes |
| total storage for all player | 50M\*32 = 1.6 GB  |

### Storage

| Desc                                     | value                     |
| ---------------------------------------- | ------------------------- |
| storage for a day all player             | 1.6 GB                    |
| total storage for all player for 5 years | 1.6 GB * 5 *365 = 2.92 TB |

### Bandwidth

**Ingress** is the network traffic that enters the server (client requests) and **Egress** is the network traffic that exits the server(server resp)

| Desc    | value                                                                    |
| ------- | ------------------------------------------------------------------------ |
| Ingress | 32 bytes/player _ 50M players/day _ 10^-5 day/sec = 16 KB/sec            |
| Egress  | 64 bytes/player _ 250 million players/day _ 10^(-5) day/sec = 160 KB/sec |

### API design

1. for each of scalability we will be using REST. as it allows loose coupling as than RPC.

#### Update player score

1. Request
   ```
   /players/:player-id/scores
   method: POST
   authrorization: Bearer <token>
   content-length: 100  (size of the request body in bytes)
   content-type: application/json
   content-encoding: gzip (lossless encoding for reduced data size)
   {
       player_id:<int>,
       score:<int>,
       location:geohash
   }
   ```
2. Response
   ```
   200 - success
   202 - accepted for async processing
   403 - Unauth
   400 - invalid payload
   ```

#### Get a single player score

1.  Request
    ```
    /players/:player-id
    method: GET
    authrorization: Bearer <token>
    accept: application/json, text/html
    user-agent: chrome
    ```
2.  Response

    ```
    status code: 200 OK
    cache-control: private, no-cache, must-revalidate, max-age=5
    content-encoding: gzip
    content-type: application/json

    {
        player_id: <string>,
        player_name: <string>,
        score: <int>,
        rank: <int>,
        updated_at: <date_timestamp>
    }
    ```

    ```
    403 - Unauth
    404 - not found
    ```

#### Get top N player

1.  Request
    ```
    /players/top/:count
    method: GET
    authrorization: Bearer <token>
    accept: application/json, text/html
    user-agent: chrome
    ```
2.  Response

    ```
    status code: 200 OK
    cache-control: public, no-cache, must-revalidate, max-age=5
    content-encoding: gzip
    content-type: application/json

    {
        count: 10(count),
        updated_at:<timestamp>,
        data: [
            {
                player_id: <string>,
                player_name: <string>,
                score: <int>,
                rank: <int>
            }
        ]
    }
    ```

    ```
    403 - Unauth
    ```

#### Get surrounding players score

1.  Request
    ```
    /players/:player-id/:count
    method: GET
    authrorization: Bearer <token>
    accept: application/json, text/html
    user-agent: chrome
    ```
2.  Response

    ```
    status code: 200 OK
    cache-control: public, no-cache, must-revalidate, max-age=5
    content-encoding: gzip
    content-type: application/json

    {
        count: 10(count),
        updated_at:<timestamp>,
        data: [
            {
                player_id: <string>,
                player_name: <string>,
                score: <int>,
                rank: <int>
            }
        ]
    }
    ```

    ```
    403 - Unauth
    ```

#### Service health

```
/:service/health
method: HEAD
```

Response

```
200 - OK
500 - error
```
