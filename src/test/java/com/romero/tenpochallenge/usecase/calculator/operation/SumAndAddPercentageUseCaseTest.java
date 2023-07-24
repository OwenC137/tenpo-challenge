package com.romero.tenpochallenge.usecase.calculator.operation;

import com.romero.tenpochallenge.usecase.calculator.percentage.ApplyPercentageUseCase;
import com.romero.tenpochallenge.usecase.calculator.percentage.GetPercentageUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.util.Pair;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
@MockBean(classes = {ApplyPercentageUseCase.class})
public class SumAndAddPercentageUseCaseTest {

    private SumAndAddPercentageUseCase sumAndAddPercentageUseCase;
    private final GetPercentageUseCase getPercentageUseCase;

    SumAndAddPercentageUseCaseTest(@Autowired GetPercentageUseCase getPercentageUseCase) {
        this.sumAndAddPercentageUseCase = new SumAndAddPercentageUseCase(getPercentageUseCase);
        this.getPercentageUseCase = getPercentageUseCase;
    }

    @Test
    public void when_sum_and_add_percentage_then_return_sum() {

        StepVerifier.create(this.sumAndAddPercentageUseCase.execute(
                Pair.of(BigDecimal.valueOf(50), BigDecimal.valueOf(50))
                ))
                .expectNextMatches(sum -> sum.compareTo(BigDecimal.valueOf(110)) == 0)
                .verifyComplete();
    }

}
