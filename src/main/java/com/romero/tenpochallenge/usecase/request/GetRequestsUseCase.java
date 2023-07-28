package com.romero.tenpochallenge.usecase.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.romero.tenpochallenge.data.model.RequestData;
import com.romero.tenpochallenge.data.repository.RequestDataRepository;
import com.romero.tenpochallenge.usecase.UseCaseWithParameters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GetRequestsUseCase implements UseCaseWithParameters<Integer, Mono<Page<RequestDataDto>>> {

    private final RequestDataRepository requestDataRepository;
    private final ObjectMapper objectMapper;

    private final static Integer PAGE_SIZE = 10;

    public GetRequestsUseCase(@Autowired final RequestDataRepository requestDataRepository,
                              @Autowired final ObjectMapper objectMapper) {
        this.requestDataRepository = requestDataRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Page<RequestDataDto>> execute(final Integer page) {
        return this.requestDataRepository.findAllBy(
                        Pageable.ofSize(PAGE_SIZE).withPage(page)
                ).map(
                        requestData -> {
                            try {
                                return this.mapRequestDataToDto(requestData);
                            } catch (final JsonProcessingException e) {
                                log.error("Error mapping request data to dto", e);
                                return RequestDataDto.builder().build();
                            }
                        }
                )
                .collectList()
                .zipWith(this.requestDataRepository.count())
                .map(items -> new PageImpl<>(items.getT1(), Pageable.ofSize(PAGE_SIZE), items.getT2()));
    }

    private RequestDataDto mapRequestDataToDto(final RequestData requestData) throws JsonProcessingException {
        return RequestDataDto.builder()
                .body(this.objectMapper.readTree(requestData.getBody()))
                .date(requestData.getDate())
                .response(this.objectMapper.readTree(requestData.getResponse()))
                .id(requestData.getId())
                .build();
    }
}
