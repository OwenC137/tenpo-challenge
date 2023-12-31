package com.romero.tenpochallenge.controller.api.calculator;

import com.romero.tenpochallenge.error.AppException;
import com.romero.tenpochallenge.model.SumAndAddPercentageRequest;
import com.romero.tenpochallenge.usecase.calculator.operation.SumAndAddPercentageOperation;
import com.romero.tenpochallenge.usecase.calculator.operation.SumAndAddPercentageUseCase;
import com.romero.tenpochallenge.usecase.request.SaveRequestUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import static reactor.core.scheduler.Schedulers.boundedElastic;

@RestController
@RequestMapping("/api/v1/calculator")
public class CalculatorApiController {

    private final SumAndAddPercentageUseCase sumAndAddPercentageUseCase;
    private final SaveRequestUseCase saveRequestUseCase;

    CalculatorApiController(@Autowired final SumAndAddPercentageUseCase sumAndAddPercentageUseCase,
                            @Autowired final SaveRequestUseCase saveRequestUseCase) {
        this.sumAndAddPercentageUseCase = sumAndAddPercentageUseCase;
        this.saveRequestUseCase = saveRequestUseCase;
    }

    @PostMapping("/sum-and-apply-percentage")
    public Mono<SumAndAddPercentageOperation> sumAndApplyPercentage(@Valid @RequestBody final SumAndAddPercentageRequest request) {
        return this.sumAndAddPercentageUseCase.execute(Pair.of(request.getFirstNumber(), request.getSecondNumber()))
                .retryWhen(Retry.backoff(3, java.time.Duration.ofMillis(100)).filter(throwable ->
                        !(throwable instanceof AppException)
                )).doOnSuccess(sumAndAddPercentageOperation ->
                        this.saveRequestUseCase.execute(Pair.of(request, sumAndAddPercentageOperation)).subscribeOn(
                                boundedElastic()
                        ).subscribe()
                );
    }
}
