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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

@ExtendWith(SpringExtension.class)
@MockBean(classes = {RequestDataRepository.class})
@Import(ObjectMapper.class)
public class GetRequestsUseCaseTest {

    private final GetRequestsUseCase getRequestUseCase;
    private final RequestDataRepository requestDataRepository;
    private final ObjectMapper objectMapper;

    GetRequestsUseCaseTest(@Autowired final RequestDataRepository requestDataRepository,
                           @Autowired final ObjectMapper objectMapper) {
        this.getRequestUseCase = new GetRequestsUseCase(requestDataRepository, objectMapper);
        this.requestDataRepository = requestDataRepository;
        this.objectMapper = objectMapper;
    }

    @Test
    public void when_get_page_of_request_then_return_page_of_request() throws JsonProcessingException {
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

        Mockito.when(this.requestDataRepository.findAllBy(Mockito.any()))
                .thenReturn(Flux.fromIterable(List.of(expected)));

        Mockito.when(this.requestDataRepository.count())
                .thenReturn(Mono.just(1L));

        StepVerifier.create(this.getRequestUseCase.execute(0))
                .expectNextCount(1)
                .verifyComplete();
    }
}
