package com.romero.tenpochallenge.cache.redis;

import com.romero.tenpochallenge.model.RateLimit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class ReactiveCacheRateLimit extends AbstractReactiveRedisTemplate<RateLimit> {
    private static final String RATE_LIMIT_CACHE_KEY = "RATE_LIMIT";

    public ReactiveCacheRateLimit(@Autowired final ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
        super(RateLimit.class, reactiveRedisConnectionFactory);
    }

    public Mono<RateLimit> getLimit() {
        return this.reactiveRedisTemplate.opsForValue().get(RATE_LIMIT_CACHE_KEY);
    }

    public Mono<RateLimit> setLimit(final RateLimit rateLimit, final Duration duration) {
        return this.reactiveRedisTemplate.opsForValue()
                .set(RATE_LIMIT_CACHE_KEY, rateLimit, duration)
                .map(__ -> rateLimit);
    }
}
