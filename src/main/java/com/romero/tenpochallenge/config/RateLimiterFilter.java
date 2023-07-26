package com.romero.tenpochallenge.config;

import com.romero.tenpochallenge.error.AppErrorCatalog;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Configuration
@Slf4j
public class RateLimiterFilter implements WebFilter {
    private final Bucket bucket;

    public RateLimiterFilter(
            @Value("${rate.limit.perMinute}") final Long limitPerMinute
    ) {
        final Bandwidth limit = Bandwidth.simple(limitPerMinute, Duration.ofMinutes(1));
        this.bucket = Bucket.builder().withNanosecondPrecision().addLimit(limit).build();
    }

    @Override
    public Mono<Void> filter(final ServerWebExchange exchange, final WebFilterChain chain) {
        final ServerHttpResponse response = exchange.getResponse();
        final ConsumptionProbe consumptionProbe = this.bucket.tryConsumeAndReturnRemaining(1);
        final Long remainingLimit = consumptionProbe.getRemainingTokens();
        if (consumptionProbe.isConsumed()) {
            log.info("Remaining {}", remainingLimit);
        } else {
            return Mono.error(AppErrorCatalog.RATE_LIMIT_EXCEEDED.toException());
        }
        response.getHeaders().set("X-Rate-Limit-Remaining", String.valueOf(remainingLimit));
        return chain.filter(exchange);
    }
}
