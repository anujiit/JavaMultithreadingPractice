package com.myjavaprojects.multithread;

public interface RateLimiter {
    boolean tryConsume();
}
