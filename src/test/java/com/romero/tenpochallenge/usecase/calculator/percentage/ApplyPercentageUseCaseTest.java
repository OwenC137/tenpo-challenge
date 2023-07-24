package com.romero.tenpochallenge.usecase.calculator.percentage;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
@MockBean(classes = {GetPercentageUseCase.class})
public class ApplyPercentageUseCaseTest {

    private final ApplyPercentageUseCase applyPercentageUseCase;
    private final GetPercentageUseCase getPercentageUseCase;


    ApplyPercentageUseCaseTest(@Autowired GetPercentageUseCase getPercentageUseCase) {
        this.applyPercentageUseCase = new ApplyPercentageUseCase(getPercentageUseCase);
        this.getPercentageUseCase = getPercentageUseCase;
    }

    @Test
    public void when_apply_percentage_then_return_sum() {
//        Mockito.when(this.getPercentageUseCase.execute()).thenReturn(
//                Mono.just(
//                        Percentage.builder()
//                                .value(BigDecimal.valueOf(10))
//                                .build()
//                )
//        );

        this.applyPercentageUseCase.execute(BigDecimal.valueOf(100))
                .as(StepVerifier::create)
                .expectNextMatches(sum -> sum.compareTo(BigDecimal.valueOf(110)) == 0)
                .verifyComplete();
    }

}
