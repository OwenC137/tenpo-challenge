package com.romero.tenpochallenge.cache.redis;

import com.romero.tenpochallenge.usecase.calculator.percentage.Percentage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class ReactiveCachePercentage {

    private static final String PERCENTAGE_CACHE_KEY = "percentage";

    private final ReactiveValueOperations<String, Percentage> reactiveValueOps;

    private final Long ttl;

    public ReactiveCachePercentage(
            @Autowired final ReactiveRedisTemplate<String, Percentage> redisTemplate,
            @Value("${cache.ttl.percentage}") final Long ttl
    ) {
        this.reactiveValueOps = redisTemplate.opsForValue();
        this.ttl = ttl;
    }

    public Mono<Percentage> getPercentage() {
        return this.reactiveValueOps.get(PERCENTAGE_CACHE_KEY);
    }

    public Mono<Percentage> setPercentage(final Percentage percentage) {
        return this.reactiveValueOps.set(PERCENTAGE_CACHE_KEY, percentage, Duration.ofMinutes(this.ttl)).map(__ -> percentage);
    }

}
