package CircuitBreaker;

import java.time.Duration;

public class CircuitBreakerDemo {

    public static void run() {
        CircuitBreaker breaker = new CircuitBreaker(Duration.ofSeconds(5), 2);

        for (int i = 0; i < 10; i++) {
            try {
                String result = breaker.call(() -> simulateExternalService());
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static String simulateExternalService() {
        if (Math.random() < 0.7) {
            throw new RuntimeException("Service failed!");
        }
        return "Success response!";
    }
}
