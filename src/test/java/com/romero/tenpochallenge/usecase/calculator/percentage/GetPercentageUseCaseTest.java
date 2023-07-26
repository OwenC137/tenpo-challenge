package com.romero.tenpochallenge.usecase.calculator.percentage;

import com.romero.tenpochallenge.cache.redis.ReactiveCachePercentage;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
    private GetPercentageUseCase getPercentageUseCase;

    private ReactiveCachePercentage reactiveCachePercentage;

    @BeforeEach
    void initialize(
            @Autowired final ReactiveCachePercentage reactiveCachePercentage
    ) {
        final String baseUrl = String.format("http://localhost:%s/percentage",
                mockBackEnd.getPort());
        this.getPercentageUseCase = new GetPercentageUseCase(baseUrl, reactiveCachePercentage);
        this.reactiveCachePercentage = reactiveCachePercentage;
    }

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    public void when_get_percentage_then_return_percentage() {
        final Mono<Percentage> percentage = Mono.just(Percentage.builder().value(BigDecimal.valueOf(0.1)).build());

        Mockito.when(this.reactiveCachePercentage.getPercentageOrDefaultAndSave(Mockito.any()))
                .thenReturn(percentage);

        mockBackEnd.enqueue(new MockResponse()
                .setBody("{\"value\": 0.1}")
                .addHeader("Content-Type", "application/json"));

        StepVerifier.create(this.getPercentageUseCase.execute())
                .expectNextMatches(matches -> matches.getValue().equals(BigDecimal.valueOf(0.1)))
                .verifyComplete();
    }
}
