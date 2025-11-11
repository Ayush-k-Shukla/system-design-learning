package CircuitBreaker;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

public class CircuitBreaker {
    private final int thresholdCount;
    private int failureCount;
    private final Duration retryTime;
    private Instant lastFailureTime;
    private CircuitBreakerState state;

    public CircuitBreaker(Duration retryTime, int thresholdCount){
        this.thresholdCount = thresholdCount;
        this.retryTime = retryTime;
        this.state = CircuitBreakerState.CLOSED;
        this.failureCount = 0;
    }


    public synchronized <T> T call(Supplier<T> action){
        if(state == CircuitBreakerState.OPEN){
            if(Instant.now().isAfter(lastFailureTime.plus(retryTime))){
                state = CircuitBreakerState.HALF_OPEN;
                System.out.println("Switching to HALF_OPEN state");
            } else {
                throw new RuntimeException("Circuit is OPEN. Call blocked!");
            }
        }

        try {
           T result = action.get();
            trackSuccess();
            return result;
        } catch (Exception e) {
            trackFailure();
            throw e;
        }
    }

    private void trackSuccess(){
        if (state == CircuitBreakerState.HALF_OPEN || state == CircuitBreakerState.OPEN) {
            System.out.println("Service recovered. Switching to CLOSED state");
        }
        state = CircuitBreakerState.CLOSED;
        failureCount = 0;
    }

    private void trackFailure(){
        failureCount++;
        lastFailureTime = Instant.now();
        if(failureCount >= thresholdCount){
            state = CircuitBreakerState.OPEN;
            System.out.println("Circuit OPENED!");
        }
    }

    public CircuitBreakerState getState() {
        return state;
    }
}
