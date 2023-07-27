package com.romero.tenpochallenge.usecase.calculator.percentage;

import com.romero.tenpochallenge.cache.redis.ReactiveCachePercentage;
import com.romero.tenpochallenge.usecase.UseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Component
@Slf4j
public class GetPercentageUseCase implements UseCase<Mono<Percentage>> {
    private final ReactiveCachePercentage reactiveCachePercentage;
    private final String percentageUrl;

    public GetPercentageUseCase(@Value("${external.percentage.url}") final String percentageUrl,
                                @Autowired final ReactiveCachePercentage reactiveCachePercentage
    ) {
        this.percentageUrl = percentageUrl;
        this.reactiveCachePercentage = reactiveCachePercentage;

    }

    @Override
    public Mono<Percentage> execute() {
        return this.reactiveCachePercentage.getPercentage()
                .switchIfEmpty(
                        this.getPercentageFromWebClient().flatMap(this.reactiveCachePercentage::setPercentage)
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
