package com.romero.tenpochallenge.usecase.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.romero.tenpochallenge.data.model.RequestData;
import com.romero.tenpochallenge.data.repository.RequestDataRepository;
import com.romero.tenpochallenge.model.SumAndAddPercentageRequest;
import com.romero.tenpochallenge.usecase.calculator.operation.SumAndAddPercentageOperation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.util.Pair;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
@MockBean(classes = {RequestDataRepository.class})
@Import(ObjectMapper.class)
public class SaveRequestUseCaseTest {

    private final SaveRequestUseCase saveRequestUseCase;
    private final RequestDataRepository requestDataRepository;
    private final ObjectMapper objectMapper;

    SaveRequestUseCaseTest(@Autowired final RequestDataRepository requestDataRepository,
                           @Autowired final ObjectMapper objectMapper) {
        this.saveRequestUseCase = new SaveRequestUseCase(requestDataRepository, objectMapper);
        this.requestDataRepository = requestDataRepository;
        this.objectMapper = objectMapper;
    }

    @Test
    public void when_request_is_saved_then_return_request() throws JsonProcessingException {
        final SumAndAddPercentageRequest request = SumAndAddPercentageRequest.builder()
                .firstNumber(BigDecimal.valueOf(1))
                .secondNumber(BigDecimal.valueOf(2))
                .build();

        final SumAndAddPercentageOperation operation = SumAndAddPercentageOperation.builder()
                .sum(BigDecimal.valueOf(3))
                .percentage(BigDecimal.valueOf(10))
                .result(BigDecimal.valueOf(3.3))
                .build();

        final RequestData expected = RequestData.builder()
                .response(this.objectMapper.writeValueAsString(operation))
                .body(this.objectMapper.writeValueAsString(request))
                .build();

        Mockito.when(this.requestDataRepository.save(Mockito.any()))
                .thenReturn(Mono.just(expected));

        StepVerifier.create(this.saveRequestUseCase.execute(
                Pair.of(request, operation)
        )).expectNextMatches(requestData ->
                requestData.getBody().equals(expected.getBody()) &&
                        requestData.getResponse().equals(expected.getResponse())
        ).verifyComplete();
    }
}
