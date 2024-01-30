package com.myjavaprojects.multithread;

import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        UserRateLimiterMap ob = new UserRateLimiterMap();

        RateLimiter rateLimiter = new FixedWindowRateLimiter(1, 1, RateLimiterTimeUnit.SECONDS);
        rateLimiter = new SlidingWindowRateLimiter(2, 5, RateLimiterTimeUnit.SECONDS);
        while (true) {
            if (rateLimiter.tryConsume()) {
                System.out.println("Timestamp: " + Instant.now().getEpochSecond());
                //Thread.sleep((long)Math.random());
            }
        }

    }
}
