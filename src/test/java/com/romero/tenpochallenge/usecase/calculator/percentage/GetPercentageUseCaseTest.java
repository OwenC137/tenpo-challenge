package com.romero.tenpochallenge.usecase.calculator.percentage;

import com.romero.tenpochallenge.cache.redis.ReactiveCachePercentage;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
@MockBean(classes = {ReactiveCachePercentage.class})
public class GetPercentageUseCaseTest {
    public static MockWebServer mockBackEnd;
    private final GetPercentageUseCase getPercentageUseCase;
    private final ReactiveCachePercentage reactiveCachePercentage;

    GetPercentageUseCaseTest(@Autowired final ReactiveCachePercentage reactiveCachePercentage) {
        final String baseUrl = String.format("http://localhost:%s/percentage",
                mockBackEnd.getPort());
        this.getPercentageUseCase = new GetPercentageUseCase(baseUrl, reactiveCachePercentage);
        this.reactiveCachePercentage = reactiveCachePercentage;
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @Test
    public void when_get_percentage_then_return_percentage() {

        Mockito.when(this.reactiveCachePercentage.getPercentage())
                .thenReturn(Mono.empty());

        Mockito.when(this.reactiveCachePercentage.setPercentage(Mockito.any()))
                .thenReturn(Mono.just(Percentage.builder().value(BigDecimal.valueOf(2)).build()));

        mockBackEnd.enqueue(new MockResponse()
                .setBody("{\"value\": 2}")
                .addHeader("Content-Type", "application/json"));

        StepVerifier.create(this.getPercentageUseCase.execute())
                .expectNextMatches(matches -> matches.getValue().equals(BigDecimal.valueOf(2)))
                .verifyComplete();
    }

    @Test
    public void when_get_percentage_from_cache_then_return_percentage() {

        Mockito.when(this.reactiveCachePercentage.getPercentage())
                .thenReturn(Mono.just(Percentage.builder().value(BigDecimal.valueOf(2)).build()));

        StepVerifier.create(this.getPercentageUseCase.execute())
                .expectNextMatches(matches -> matches.getValue().equals(BigDecimal.valueOf(2)))
                .verifyComplete();

        Mockito.verify(this.reactiveCachePercentage, Mockito.times(0)).setPercentage(Mockito.any());
    }

}
