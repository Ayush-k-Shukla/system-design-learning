# Redis Sorted Sets

## 1. Why It Is Used

1. Redis sorted sets are a data structure that store unique elements, each associated with a floating-point score. The elements are automatically sorted based on their scores, making sorted sets useful for scenarios requiring ordered data retrieval and efficient range queries.
2. If two members have the same score, Redis sorts them based on members’ lexicographical order.

## 2. Use Cases

Sorted sets are commonly used for:

- **Leaderboards**: Maintaining dynamically updated rankings (e.g., gaming scores, competition rankings).
- **Rate Limiting**: Implementing sliding-window rate limiting to control API request rates.
- **Priority Queues**: Managing tasks based on priority.
- **Ranking Systems**: Keeping track of top performers or trending items in real-time applications.

## 3. Practice Examples

### Adding Elements to a Sorted Set

```sh
ZADD <name_of_sorted_set> <score> <member>
```

```sh
ZADD leaderboard 100 "Alice"
ZADD leaderboard 200 "Bob"
```

### Change value of score for a member

```sh
ZINCRBY <name_of_sorted_set> <increment> <member>
```

```sh
ZINCRBY players 10 "Alice"
ZINCRBY players -10 "Bob"
```

### Retrieving the Top Players

returns members in reversed-rank order, with scores ordered from high to low. You have to specify starting and ending rank index positions with a start and stop parameters.

```sh
ZREVRANGE <key> <start> <stop> [WITHSCORES]
```

```sh
// for top 10 players
ZREVRANGE leaderboard 0 9 WITHSCORES
```

### Retrieve the rank and the score of an individual player

```sh
[ZRANK|ZREVRANK] <key> <member>
```

### Removing a Player

```sh
ZREM <key> <member> [<member> …]
```

```sh
ZREM leaderboard "Alice"
```

## 4. Time Complexity

- **ZADD**: O(log N) (Adding an element)
- **ZRANGE**: O(log N + M) (Retrieving elements in a range, where M is the number of results)
- **ZRANGEBYSCORE**: O(log N + M)
- **ZREM**: O(log N) (Removing an element)
- **ZREVRANK**: O(log N) (Getting reverse rank of an element)

Redis sorted sets efficiently handle ordered data, making them ideal for real-time applications requiring fast insertions, deletions, and queries.
