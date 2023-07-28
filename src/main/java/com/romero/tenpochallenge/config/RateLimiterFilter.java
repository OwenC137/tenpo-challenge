package com.romero.tenpochallenge.config;

import com.romero.tenpochallenge.cache.redis.ReactiveCacheRateLimit;
import com.romero.tenpochallenge.error.AppErrorCatalog;
import com.romero.tenpochallenge.model.RateLimit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Configuration
@Slf4j
public class RateLimiterFilter implements WebFilter {

    private final ReactiveCacheRateLimit reactiveCacheRateLimit;

    private final Integer amount;
    private final ChronoUnit unit;

    public RateLimiterFilter(final ReactiveCacheRateLimit reactiveCacheRateLimit,
                             @Value("${rate.limit.amount}") final Integer amount,
                             @Value("${rate.limit.unit}") final ChronoUnit unit) {
        this.reactiveCacheRateLimit = reactiveCacheRateLimit;
        this.amount = amount;
        this.unit = unit;
    }

    @Override
    public Mono<Void> filter(final ServerWebExchange exchange, final WebFilterChain chain) {

        if (!exchange.getRequest().getURI().getPath().contains("/api")) {
            return chain.filter(exchange);
        }

        return this.reactiveCacheRateLimit.getLimit().defaultIfEmpty(this.defaultRateLimit())
                .flatMap(rateLimit -> {

                    if (Instant.now().toEpochMilli() > rateLimit.getExpiration()) {
                        rateLimit.setExpiration(Instant.now().plus(1, this.unit).toEpochMilli());
                        rateLimit.setRemaining(this.amount);
                        log.info("Rate limit reset");
                        return this.reactiveCacheRateLimit.setLimit(rateLimit, Duration.of(1, this.unit));
                    }

                    if (rateLimit.getRemaining() > 0) {
                        rateLimit.setRemaining(rateLimit.getRemaining() - 1);
                        log.info("Rate limit remaining: {}", rateLimit.getRemaining());
                        return this.reactiveCacheRateLimit.setLimit(rateLimit, Duration.of(1, this.unit));
                    }
                    log.warn("Rate limit exceeded");
                    return Mono.error(AppErrorCatalog.RATE_LIMIT_EXCEEDED.toException());
                }).then(chain.filter(exchange));
    }

    private RateLimit defaultRateLimit() {
        return RateLimit.builder()
                .limit(this.amount)
                .remaining(this.amount)
                .expiration(Instant.now().plus(1, this.unit).toEpochMilli())
                .build();
    }
}
