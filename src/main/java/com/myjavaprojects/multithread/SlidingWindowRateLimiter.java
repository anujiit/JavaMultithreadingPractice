package com.myjavaprojects.multithread;

import java.time.Instant;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class SlidingWindowRateLimiter implements RateLimiter{

    private BlockingQueue<Long> window;
    private final int intervalTime;
    private final int maxRequestsPerInterval;
    private final RateLimiterTimeUnit timeUnit;

    public SlidingWindowRateLimiter(int intervalTime, int maxRequestsPerInterval, RateLimiterTimeUnit timeUnit) {
        this.intervalTime = intervalTime;
        this.maxRequestsPerInterval = maxRequestsPerInterval;

        this.timeUnit = timeUnit;
        this.window = new LinkedBlockingDeque<>();
    }

    @Override
    public synchronized boolean tryConsume() {
        long currentTime = computeCurrentTimeBasedOnTimeUnit();

        if (this.window.isEmpty()) {
            //System.out.println("currentTime: "+currentTime);
            this.window.add(currentTime);
            return true;
        }

        // clear expired requests
        while (!this.window.isEmpty() && currentTime - this.window.peek() +1 > intervalTime) {
            this.window.poll();
        }
        boolean status = false;
        if (this.window.size()< maxRequestsPerInterval) {
            //System.out.println("currentTime: "+currentTime);
            this.window.offer(currentTime);
            status = true;
        }

        return status;
    }

    private long computeCurrentTimeBasedOnTimeUnit() {
        if (this.timeUnit == RateLimiterTimeUnit.SECONDS) {
            return Instant.now().getEpochSecond();
        } else if (this.timeUnit == RateLimiterTimeUnit.MINUTES) {
            return Instant.now().getEpochSecond() / 60;
        } else if (this.timeUnit == RateLimiterTimeUnit.HOUR) {
            return Instant.now().getEpochSecond() / 3600;
        } else {
            return Instant.now().getEpochSecond();
        }
    }
}
