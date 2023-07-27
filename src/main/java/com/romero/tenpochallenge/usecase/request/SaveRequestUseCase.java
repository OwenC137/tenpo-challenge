package com.romero.tenpochallenge.usecase.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.romero.tenpochallenge.data.repository.HttpRequestDataRepository;
import com.romero.tenpochallenge.model.SumAndAddPercentageRequest;
import com.romero.tenpochallenge.usecase.UseCaseWithParameters;
import com.romero.tenpochallenge.usecase.calculator.operation.SumAndAddPercentageOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class SaveRequestUseCase implements UseCaseWithParameters<Pair<SumAndAddPercentageRequest,
        SumAndAddPercentageOperation>, Mono<Void>> {
    private final HttpRequestDataRepository httpRequestDataRepository;
    private final ObjectMapper objectMapper;

    public SaveRequestUseCase(
            @Autowired final HttpRequestDataRepository httpRequestDataRepository,
            @Autowired final ObjectMapper objectMapper) {
        this.httpRequestDataRepository = httpRequestDataRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> execute(final Pair<SumAndAddPercentageRequest, SumAndAddPercentageOperation> parameter) {
        log.info("Saving request");
        return Mono.empty();
    }
}
