package com.romero.tenpochallenge.controller.api.calculator;

import com.romero.tenpochallenge.usecase.request.GetRequestsUseCase;
import com.romero.tenpochallenge.usecase.request.RequestDataDto;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/request-history")
public class RequestApiController {

    private final GetRequestsUseCase getRequestsUseCase;

    public RequestApiController(final GetRequestsUseCase getRequestsUseCase) {
        this.getRequestsUseCase = getRequestsUseCase;
    }

    @GetMapping
    public Mono<Page<RequestDataDto>> getAll(@RequestParam(defaultValue = "0") final Integer page) {
        return this.getRequestsUseCase.execute(page);
    }
}
