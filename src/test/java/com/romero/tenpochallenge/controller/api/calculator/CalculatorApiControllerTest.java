package com.romero.tenpochallenge.controller.api.calculator;

import com.romero.tenpochallenge.config.RateLimiterFilter;
import com.romero.tenpochallenge.model.SumAndAddPercentageRequest;
import com.romero.tenpochallenge.usecase.calculator.operation.SumAndAddPercentageOperation;
import com.romero.tenpochallenge.usecase.calculator.operation.SumAndAddPercentageUseCase;
import io.github.bucket4j.local.LockFreeBucket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;

@RunWith(SpringRunner.class)
@WebFluxTest(CalculatorApiController.class)
@MockBean(classes = {SumAndAddPercentageUseCase.class})
public class CalculatorApiControllerTest {

    private final WebTestClient webTestClient;
    private final SumAndAddPercentageUseCase sumAndAddPercentageUseCase;
    private final RateLimiterFilter rateLimiterFilter;

    CalculatorApiControllerTest(
            @Autowired final WebTestClient webTestClient,
            @Autowired final SumAndAddPercentageUseCase sumAndAddPercentageUseCase,
            @Autowired final RateLimiterFilter rateLimiterFilter
    ) {
        this.webTestClient = webTestClient;
        this.sumAndAddPercentageUseCase = sumAndAddPercentageUseCase;
        this.rateLimiterFilter = rateLimiterFilter;
    }

    //    every test execute code before
    @BeforeEach
    public void setUp() {
        final LockFreeBucket bucket = (LockFreeBucket) ReflectionTestUtils.getField(this.rateLimiterFilter, "bucket");
        assert bucket != null;
        bucket.reset();
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

        this.webTestClient.post()
                .uri("/api/calculator/sum-and-apply-percentage")
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
        Mockito.when(this.sumAndAddPercentageUseCase.execute(Mockito.any()))
                .thenReturn(Mono.just(
                                SumAndAddPercentageOperation.builder()
                                        .sum(BigDecimal.valueOf(100))
                                        .percentage(BigDecimal.valueOf(10))
                                        .result(BigDecimal.valueOf(110))
                                        .build()
                        )
                );
        this.webTestClient.post()
                .uri("/api/calculator/sum-and-apply-percentage")
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

        this.webTestClient.post()
                .uri("/api/calculator/sum-and-apply-percentage")
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

        this.webTestClient.post()
                .uri("/api/calculator/sum-and-apply-percentage")
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

//        now must fail for exceed the limit
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
