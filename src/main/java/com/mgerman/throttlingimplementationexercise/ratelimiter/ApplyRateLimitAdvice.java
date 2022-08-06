package com.mgerman.throttlingimplementationexercise.ratelimiter;

import com.mgerman.throttlingimplementationexercise.ratelimiter.exception.RequestRateLimitException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class ApplyRateLimitAdvice {

    @Value("${requests-count-per-minute}")
    private long requestsCountPerMinute;
    @Value("${throttling-period-in-minutes}")
    private long periodInMinutes;

    ConcurrentHashMap<String, RateLimiter> ipToRateLimiterMap = new ConcurrentHashMap<>();

    @Before("@annotation(ApplyRateLimiter)")
    public void tryAccessToMethod() throws RequestRateLimitException {
        var request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        ipToRateLimiterMap.computeIfAbsent(request.getRemoteAddr(), k -> new RateLimiter(requestsCountPerMinute, Duration.ofMinutes(periodInMinutes)));
        RateLimiter rateLimiter = ipToRateLimiterMap.get(request.getRemoteAddr());
        if (!rateLimiter.isRequestAllowed()) {
            throw new RequestRateLimitException();
        }
    }
}
