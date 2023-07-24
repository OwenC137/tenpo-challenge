package com.romero.tenpochallenge.usecase.calculator.percentage;

import com.romero.tenpochallenge.usecase.UseCaseWithParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class ApplyPercentageUseCase implements UseCaseWithParameters<BigDecimal, Mono<BigDecimal>> {

    private final GetPercentageUseCase getPercentageUseCase;

    public ApplyPercentageUseCase(@Autowired GetPercentageUseCase getPercentageUseCase) {
        this.getPercentageUseCase = getPercentageUseCase;
    }

    @Override
    public Mono<BigDecimal> execute(BigDecimal parameter) {
        return this.getPercentageUseCase.execute().map(
                percentage -> percentage.getValue().divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING)
                        .add(BigDecimal.ONE)
                        .multiply(parameter)
        );
    }
}
