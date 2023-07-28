package com.romero.tenpochallenge.controller.api.calculator;

import com.romero.tenpochallenge.cache.redis.ReactiveCacheRateLimit;
import com.romero.tenpochallenge.config.RateLimiterFilter;
import com.romero.tenpochallenge.model.RateLimit;
import com.romero.tenpochallenge.usecase.request.GetRequestsUseCase;
import com.romero.tenpochallenge.usecase.request.RequestDataDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@RunWith(SpringRunner.class)
@WebFluxTest(RequestApiController.class)
@MockBean(classes = {GetRequestsUseCase.class, ReactiveCacheRateLimit.class})
public class RequestApiControllerTest {
    private final WebTestClient webTestClient;

    private final GetRequestsUseCase getRequestsUseCase;

    private final RateLimiterFilter rateLimiterFilter;

    private final ReactiveCacheRateLimit reactiveCacheRateLimit;

    RequestApiControllerTest(
            @Autowired final WebTestClient webTestClient,
            @Autowired final GetRequestsUseCase getRequestsUseCase,
            @Autowired final ReactiveCacheRateLimit reactiveCacheRateLimit,
            @Autowired final RateLimiterFilter rateLimiterFilter
    ) {
        this.webTestClient = webTestClient;
        this.getRequestsUseCase = getRequestsUseCase;
        this.rateLimiterFilter = rateLimiterFilter;
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
    public void when_get_request_page_then_return_page_of_request() {
        final Page<RequestDataDto> page = new PageImpl<>(
                List.of(RequestDataDto.builder().build()),
                Pageable.ofSize(1),
                1
        );

        Mockito.when(this.getRequestsUseCase.execute(Mockito.any()))
                .thenReturn(Mono.just(page));

        this.webTestClient.get()
                .uri("/api/request-history")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.content").isArray();
    }

}
