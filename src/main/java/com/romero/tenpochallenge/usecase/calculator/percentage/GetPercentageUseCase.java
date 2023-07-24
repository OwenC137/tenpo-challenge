package com.romero.tenpochallenge.usecase.calculator.percentage;

import com.romero.tenpochallenge.usecase.UseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Component
@Slf4j
public class GetPercentageUseCase implements UseCase<Mono<Percentage>> {
    private final static String PERCENTAGE_CACHE_KEY = "percentage";
    private final String percentageUrl;
    private final ReactiveValueOperations<String, Percentage> reactiveValueOps;

    public GetPercentageUseCase(@Value("${external.percentage.url}") final String percentageUrl,
                                @Autowired final ReactiveRedisTemplate<String, Percentage> redisTemplate
    ) {
        this.percentageUrl = percentageUrl;
        this.reactiveValueOps = redisTemplate.opsForValue();

    }

    @Scheduled(fixedRateString = "${cache.clear.rate}")
    public static void clearCache() {
        log.info("Clearing percentage cache");
    }

    @Override
    public Mono<Percentage> execute() {
        return this.reactiveValueOps.get(PERCENTAGE_CACHE_KEY)
                .switchIfEmpty(
                        this.getPercentageFromWebClient()
                                .flatMap(percentage -> this.reactiveValueOps.set(
                                                PERCENTAGE_CACHE_KEY,
                                                percentage,
                                                Duration.ofSeconds(60))
                                        .then(Mono.just(percentage))
                                )
                );
    }

    private Mono<Percentage> getPercentageFromWebClient() {
        final WebClient webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create()
                                .wiretap(true)))
                .build();
        return webClient.get().uri(this.percentageUrl).retrieve().bodyToMono(Percentage.class);
    }
}
