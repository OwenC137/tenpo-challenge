package com.romero.tenpochallenge.usecase.calculator.operation;

import com.romero.tenpochallenge.usecase.calculator.percentage.GetPercentageUseCase;
import com.romero.tenpochallenge.usecase.calculator.percentage.Percentage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.util.Pair;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
@MockBean(classes = {GetPercentageUseCase.class})
public class SumAndAddPercentageUseCaseTest {

    private final SumAndAddPercentageUseCase sumAndAddPercentageUseCase;
    private final GetPercentageUseCase getPercentageUseCase;

    SumAndAddPercentageUseCaseTest(@Autowired final GetPercentageUseCase getPercentageUseCase) {
        this.sumAndAddPercentageUseCase = new SumAndAddPercentageUseCase(getPercentageUseCase);
        this.getPercentageUseCase = getPercentageUseCase;
    }

    @Test
    public void when_sum_and_add_percentage_then_return_sum() {
        Mockito.when(this.getPercentageUseCase.execute())
                .thenReturn(Mono.just(Percentage.builder().value(BigDecimal.valueOf(10)).build()));

        StepVerifier.create(this.sumAndAddPercentageUseCase.execute(
                        Pair.of(BigDecimal.valueOf(50), BigDecimal.valueOf(50))
                ))
                .expectNextMatches(sum ->
                        sum.getSum().compareTo(BigDecimal.valueOf(100)) == 0 &&
                                sum.getPercentage().compareTo(BigDecimal.valueOf(10)) == 0 &&
                                sum.getResult().compareTo(BigDecimal.valueOf(110)) == 0
                )
                .verifyComplete();
    }
}
