package com.romero.tenpochallenge.usecase.calculator.percentage;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
public class GetPercentageUseCaseTest {
    public static MockWebServer mockBackEnd;
    private GetPercentageUseCase getPercentageUseCase;

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s/percentage",
                mockBackEnd.getPort());
        this.getPercentageUseCase = new GetPercentageUseCase(baseUrl,null);
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
        mockBackEnd.enqueue(new MockResponse()
                .setBody("{\"value\": 0.1}")
                .addHeader("Content-Type", "application/json"));

        StepVerifier.create(this.getPercentageUseCase.execute())
                .expectNextMatches(percentage -> percentage.getValue().equals(BigDecimal.valueOf(0.1)))
                .verifyComplete();
    }
}
