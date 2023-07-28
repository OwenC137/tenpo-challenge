package com.romero.tenpochallenge.cache.redis;

import com.romero.tenpochallenge.usecase.calculator.percentage.Percentage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class ReactiveCachePercentage extends AbstractReactiveRedisTemplate<Percentage> {

    private static final String PERCENTAGE_CACHE_KEY = "percentage";

    private final Long ttl;

    public ReactiveCachePercentage(@Value("${cache.ttl.percentage}") final Long ttl,
                                   @Autowired final ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
        super(Percentage.class, reactiveRedisConnectionFactory);
        this.ttl = ttl;
    }

    public Mono<Percentage> getPercentage() {
        return this.reactiveRedisTemplate.opsForValue().get(PERCENTAGE_CACHE_KEY);
    }

    public Mono<Percentage> setPercentage(final Percentage percentage) {
        return this.reactiveRedisTemplate.opsForValue()
                .set(PERCENTAGE_CACHE_KEY, percentage, Duration.ofMinutes(this.ttl)).map(__ -> percentage);
    }

}
