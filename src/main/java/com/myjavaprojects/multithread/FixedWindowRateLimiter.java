package com.myjavaprojects.multithread;

import java.time.Instant;

public class FixedWindowRateLimiter implements RateLimiter{

    private final int intervalTime;
    private int tokensRemaining;
    private final int maxRequestsPerInterval;
    private long lastSuccessfulTokenGeneratedTime;
    private final RateLimiterTimeUnit timeUnit;
    public FixedWindowRateLimiter(int intervalTime, int maxRequestsPerInterval, RateLimiterTimeUnit timeUnit) {

        this.intervalTime = intervalTime;
        this.maxRequestsPerInterval = maxRequestsPerInterval;
        this.tokensRemaining = maxRequestsPerInterval;

        this.timeUnit = timeUnit;
        this.lastSuccessfulTokenGeneratedTime = computeCurrentTimeBasedOnTimeUnit();
    }

    @Override
    public synchronized boolean tryConsume() {
        this.tryGeneratingTokens();
        if (this.tokensRemaining <= 0) {
            return false;
        }
        this.tokensRemaining--;
        return true;
    }

    private void tryGeneratingTokens() {
        long currentTimestamp = computeCurrentTimeBasedOnTimeUnit();
        if (currentTimestamp >= this.lastSuccessfulTokenGeneratedTime + intervalTime) {
            this.tokensRemaining = this.maxRequestsPerInterval;
            this.lastSuccessfulTokenGeneratedTime = currentTimestamp;
        }
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
