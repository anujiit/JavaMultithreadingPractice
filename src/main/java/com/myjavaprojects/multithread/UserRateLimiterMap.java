package com.myjavaprojects.multithread;

import java.util.HashMap;

public class UserRateLimiterMap {
    private HashMap<String, RateLimiter> map;

    UserRateLimiterMap() {
        map = new HashMap<>();
    }

    public void setRateLimiter(String id, ) {
        map.put(id, new SlidingWindowRateLimiter())
    }

    public void consume(String id) {
        map.get(id).tryConsume();
    }
}
