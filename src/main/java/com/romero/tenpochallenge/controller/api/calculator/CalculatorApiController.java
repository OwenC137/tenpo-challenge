package com.romero.tenpochallenge.controller.api.calculator;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.romero.tenpochallenge.usecase.calculator.operation.SumAndAddPercentageOperation;
import com.romero.tenpochallenge.usecase.calculator.operation.SumAndAddPercentageUseCase;
import com.romero.tenpochallenge.usecase.calculator.percentage.GetPercentageUseCase;
import com.romero.tenpochallenge.usecase.calculator.percentage.Percentage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;

@RestController
@RequestMapping("/api/calculator")
public class CalculatorApiController {

    private final SumAndAddPercentageUseCase sumAndAddPercentageUseCase;
    private final GetPercentageUseCase getPercentageUseCase;

    CalculatorApiController(@Autowired SumAndAddPercentageUseCase sumAndAddPercentageUseCase,
                            @Autowired GetPercentageUseCase getPercentageUseCase) {
        this.sumAndAddPercentageUseCase = sumAndAddPercentageUseCase;
        this.getPercentageUseCase = getPercentageUseCase;
    }

    @GetMapping("/sum-and-apply-percentage")
    public Mono<SumAndAddPercentageOperation> sumAndApplyPercentage() {
        return this.sumAndAddPercentageUseCase.execute(
                Pair.of(BigDecimal.ONE, BigDecimal.TEN)
        );
    }

    @GetMapping("/test")
    public Mono<Percentage> test(){
        return this.getPercentageUseCase.execute();
    }
}
