package com.romero.tenpochallenge.usecase.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.romero.tenpochallenge.data.model.RequestData;
import com.romero.tenpochallenge.data.repository.RequestDataRepository;
import com.romero.tenpochallenge.model.SumAndAddPercentageRequest;
import com.romero.tenpochallenge.usecase.UseCaseWithParameters;
import com.romero.tenpochallenge.usecase.calculator.operation.SumAndAddPercentageOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Component
public class SaveRequestUseCase implements UseCaseWithParameters<Pair<SumAndAddPercentageRequest,
        SumAndAddPercentageOperation>, Mono<RequestData>> {
    private final RequestDataRepository requestDataRepository;
    private final ObjectMapper objectMapper;

    public SaveRequestUseCase(
            @Autowired final RequestDataRepository requestDataRepository,
            @Autowired final ObjectMapper objectMapper) {
        this.requestDataRepository = requestDataRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<RequestData> execute(final Pair<SumAndAddPercentageRequest, SumAndAddPercentageOperation> parameter) {
        log.info("Saving request");
        try {
            return this.requestDataRepository.save(RequestData.builder()
                    .body(this.objectMapper.writeValueAsString(parameter.getFirst()))
                    .response(this.objectMapper.writeValueAsString(parameter.getSecond()))
                    .date(LocalDateTime.now())
                    .build());
        } catch (final JsonProcessingException e) {
            log.error("Error saving request", e);
            return Mono.empty();
        }
    }
}
