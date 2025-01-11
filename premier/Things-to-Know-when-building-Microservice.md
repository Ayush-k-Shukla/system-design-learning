# Things to Know when building Microservice

1. Micro service architecture (MSA) creates small modular services which belong to single application
2. One major advantage of MSA is like now independent services can be re-written in different language  which is best for service.
3. we can deploy as well one service without affecting other as long as external interface (API contract) is not modified.
4. Issues and resolutions
    1. Nano service hell
        1. it should be like “Billing”, “Payment” service not like “getUserById”, “getUser” service
        2. point is don’t build too much small services that outweigh the benefits 
    2. Use OpenAPI spec in order to make api contract relevant and up to date
    3. Use Service discovery tools for endpoint finding based on environment
    4. We can automate deployment jobs using jenkins
    5. One service fails, everything fails
        1. we can mitigate these by following ways - 
        2. Retries - have a retry logic with fixed number of time, use exponential backoff between tries.
        3. Circuit breaker
            1. if service continuously return error then we need to make a circuit breaker that reduce bombarding request  
                
                ![https://i1.wp.com/cloudncode.files.wordpress.com/2016/07/circuit-breaker.png](https://i1.wp.com/cloudncode.files.wordpress.com/2016/07/circuit-breaker.png)
                
    6. Database per service
        1. allows flexibility to deploy , elastic search independently per service
    7. Schema per service
        1. promote loose coupling
    8. Data warehousing
        1. like for each service we have different DB used so for analytical purpose we can choose to give to each service to push data in given format to data house.
    9. Too many servers, too much cost
        1. Yes and no, it is true that a monolith service would consume server resources more efficiently and will be easier to write deployment scripts for, however, where MSA really shines is the reduction of everyday costs for development and faster time to market by employing one time extra setup costs which is one of the major reasons to go for MSA to begin with.