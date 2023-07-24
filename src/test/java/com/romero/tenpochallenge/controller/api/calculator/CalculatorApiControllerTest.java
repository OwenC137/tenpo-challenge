package com.romero.tenpochallenge.controller.api.calculator;

import com.romero.tenpochallenge.usecase.calculator.operation.SumAndAddPercentageUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
@WebFluxTest(CalculatorApiController.class)
@MockBean(classes = {SumAndAddPercentageUseCase.class})
public class CalculatorApiControllerTest {

    private final WebTestClient webTestClient;
    private final SumAndAddPercentageUseCase sumAndAddPercentageUseCase;

    CalculatorApiControllerTest(
            @Autowired WebTestClient webTestClient,
            @Autowired SumAndAddPercentageUseCase sumAndAddPercentageUseCase
    ) {
        this.webTestClient = webTestClient;
        this.sumAndAddPercentageUseCase = sumAndAddPercentageUseCase;
    }

    @Test
    public void when_sum_two_numbers_with_percent_addition_then_return_a_number_greater_than_sum() {


        this.webTestClient.get()
                .uri("/api/calculator/sum-and-apply-percentage/50/50")
                .exchange()
                .expectStatus().isOk()
                .expectBody(BigDecimal.class)
                .isEqualTo(BigDecimal.valueOf(110));
    }
}
