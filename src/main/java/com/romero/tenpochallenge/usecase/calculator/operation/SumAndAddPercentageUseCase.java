package com.romero.tenpochallenge.usecase.calculator.operation;

import com.romero.tenpochallenge.usecase.UseCaseWithParameters;
import com.romero.tenpochallenge.usecase.calculator.percentage.GetPercentageUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class SumAndAddPercentageUseCase implements UseCaseWithParameters<Pair<BigDecimal, BigDecimal>,
        Mono<SumAndAddPercentageOperation>> {

    private final GetPercentageUseCase getPercentageUseCase;

    public SumAndAddPercentageUseCase(@Autowired final GetPercentageUseCase getPercentageUseCase) {
        this.getPercentageUseCase = getPercentageUseCase;
    }

    @Override
    public Mono<SumAndAddPercentageOperation> execute(final Pair<BigDecimal, BigDecimal> parameter) {
        final BigDecimal amount = parameter.getFirst().add(parameter.getSecond());
        return this.getPercentageUseCase.execute().map(
                percentage -> SumAndAddPercentageOperation.builder()
                        .sum(amount)
                        .percentage(percentage.getValue())
                        .result(this.sumAndAddPercentage(amount, percentage.getValue()))
                        .build()
        );
    }

    private BigDecimal sumAndAddPercentage(final BigDecimal amount, final BigDecimal percentage) {
        return percentage.divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING)
                .add(BigDecimal.ONE)
                .multiply(amount);
    }
}
