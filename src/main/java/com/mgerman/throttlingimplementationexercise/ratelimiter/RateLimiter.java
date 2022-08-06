package com.mgerman.throttlingimplementationexercise.ratelimiter;

import java.time.Duration;

public class RateLimiter {

    private final long capacity;
    private long availableRequestsCount;

    private final long timeToGenerateOneRequestInBucket;
    private long lastRefillBucketTime;


    public RateLimiter(long countOfRequests, Duration period) {
        this.capacity = countOfRequests;
        this.availableRequestsCount = countOfRequests;

        this.timeToGenerateOneRequestInBucket = period.toNanos() / countOfRequests;
        this.lastRefillBucketTime = System.nanoTime();
    }

    public synchronized boolean isRequestAllowed() {
        refillBucket();
        if (availableRequestsCount < 1) {
            return false;
        } else {
            availableRequestsCount -= 1;
            return true;
        }
    }

    private void refillBucket() {
        long durationSinceLastBucketRefill = System.nanoTime() - this.lastRefillBucketTime;
        if (durationSinceLastBucketRefill <= timeToGenerateOneRequestInBucket) {
            return;
        }
        long availableRequestSinceLastRefill = durationSinceLastBucketRefill / timeToGenerateOneRequestInBucket;
        availableRequestsCount = Math.min(capacity, availableRequestsCount + availableRequestSinceLastRefill);
        lastRefillBucketTime += availableRequestSinceLastRefill * timeToGenerateOneRequestInBucket;
    }

}
