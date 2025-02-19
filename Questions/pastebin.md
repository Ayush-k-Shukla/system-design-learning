# Pastebin

## Requirements

### Functional

1. User should be able to paste data upto 10MB size
2. Customized urls
3. Expiration
4. User login / anonymous

### Non functional

1. High availability
2. Low latency
3. Durability

## Estimation

1. Per day write - 100K
2. read/write ratio - 10:1
3. Avg paste size - 100 KB
4. Max paste size - 10 MB

### Traffic

| Desc           | value                                |
| -------------- | ------------------------------------ |
| QPS(write)     | (100k)/(sec in one day) = 2 (approx) |
| QPS(read)      | 2\*10 = 20                           |
| Peak QPS(read) | 40 (assumption)                      |

### Storage

| Desc                    | value                |
| ----------------------- | -------------------- |
| storage per day(worst)  | 10MBx100k = 1 TB     |
| storage per day(avg)    | 100KBx100k = 10 GB   |
| storage for 1 year(avg) | 10 GB \*365 = 3.6 TB |

## Database

1. As there it is going to be a lot of data so we need to make decision which DB we want to use. I am planning to choose SQL because of this point
   1. Strict schema
   2. relational data
   3. need of complex joins
   4. lookup by index
