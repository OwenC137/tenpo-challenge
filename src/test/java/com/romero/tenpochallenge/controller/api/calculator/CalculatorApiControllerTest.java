package com.romero.tenpochallenge.controller.api.calculator;

import com.romero.tenpochallenge.cache.redis.ReactiveCacheRateLimit;
import com.romero.tenpochallenge.model.RateLimit;
import com.romero.tenpochallenge.model.SumAndAddPercentageRequest;
import com.romero.tenpochallenge.usecase.calculator.operation.SumAndAddPercentageOperation;
import com.romero.tenpochallenge.usecase.calculator.operation.SumAndAddPercentageUseCase;
import com.romero.tenpochallenge.usecase.request.SaveRequestUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;

@RunWith(SpringRunner.class)
@WebFluxTest(CalculatorApiController.class)
@MockBean(classes = {SumAndAddPercentageUseCase.class, SaveRequestUseCase.class, ReactiveCacheRateLimit.class})
public class CalculatorApiControllerTest {

    private final WebTestClient webTestClient;
    private final SumAndAddPercentageUseCase sumAndAddPercentageUseCase;

    private final SaveRequestUseCase saveRequestUseCase;
    private final ReactiveCacheRateLimit reactiveCacheRateLimit;

    CalculatorApiControllerTest(
            @Autowired final WebTestClient webTestClient,
            @Autowired final SumAndAddPercentageUseCase sumAndAddPercentageUseCase,
            @Autowired final ReactiveCacheRateLimit reactiveCacheRateLimit,
            @Autowired final SaveRequestUseCase saveRequestUseCase
    ) {
        this.webTestClient = webTestClient;
        this.sumAndAddPercentageUseCase = sumAndAddPercentageUseCase;
        this.saveRequestUseCase = saveRequestUseCase;
        this.reactiveCacheRateLimit = reactiveCacheRateLimit;
    }

    @BeforeEach
    public void setUp() {
        final RateLimit rateLimit = RateLimit.builder()
                .remaining(3)
                .limit(3)
                .expiration(Instant.now().plus(Duration.ofDays(1)).toEpochMilli())
                .build();
        Mockito.when(this.reactiveCacheRateLimit.getLimit())
                .thenReturn(Mono.just(rateLimit));

        Mockito.when(this.reactiveCacheRateLimit.setLimit(Mockito.any(), Mockito.any()))
                .thenReturn(Mono.just(rateLimit));
    }

    @Test
    public void when_sum_two_numbers_with_percent_addition_then_return_a_number_greater_than_sum() {
        Mockito.when(this.sumAndAddPercentageUseCase.execute(Mockito.any()))
                .thenReturn(Mono.just(
                        SumAndAddPercentageOperation.builder()
                                .sum(BigDecimal.valueOf(100))
                                .percentage(BigDecimal.valueOf(10))
                                .result(BigDecimal.valueOf(110))
                                .build()
                ));

        Mockito.when(this.saveRequestUseCase.execute(Mockito.any()))
                .thenReturn(Mono.empty());

        this.webTestClient.post()
                .uri("/api/v1/calculator/sum-and-apply-percentage")
                .body(Mono.just(
                        SumAndAddPercentageRequest.builder()
                                .firstNumber(BigDecimal.valueOf(50))
                                .secondNumber(BigDecimal.valueOf(50))
                                .build()
                ), SumAndAddPercentageRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.sum").isEqualTo(100)
                .jsonPath("$.percentage").isEqualTo(10)
                .jsonPath("$.result").isEqualTo(110);
    }

    @Test
    public void when_use_case_retry_exceed_times_then_it_must_fail() {
        Mockito.when(this.sumAndAddPercentageUseCase.execute(Mockito.any()))
                .thenReturn(Mono.error(new RuntimeException("Error")));

        Mockito.when(this.saveRequestUseCase.execute(Mockito.any()))
                .thenReturn(Mono.empty());

        this.webTestClient.mutate().responseTimeout(Duration.ofSeconds(60)).build().post()
                .uri("/api/calculator/sum-and-apply-percentage")
                .body(Mono.just(
                        SumAndAddPercentageRequest.builder()
                                .firstNumber(BigDecimal.valueOf(50))
                                .secondNumber(BigDecimal.valueOf(50))
                                .build()
                ), SumAndAddPercentageRequest.class)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    public void when_calculator_exceed_too_many_request_per_period_it_must_fail() {
        final RateLimit rateLimit = RateLimit.builder()
                .remaining(0)
                .limit(3)
                .expiration(Instant.now().plus(Duration.ofDays(1)).toEpochMilli())
                .build();
        Mockito.when(this.reactiveCacheRateLimit.getLimit())
                .thenReturn(Mono.just(rateLimit));

        this.webTestClient.post()
                .uri("/api/calculator/sum-and-apply-percentage")
                .body(Mono.just(
                        SumAndAddPercentageRequest.builder()
                                .firstNumber(BigDecimal.valueOf(50))
                                .secondNumber(BigDecimal.valueOf(50))
                                .build()
                ), SumAndAddPercentageRequest.class)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
    }
}
