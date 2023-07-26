package com.romero.tenpochallenge.error;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.romero.tenpochallenge.error.AppErrorCatalog.RETRY_EXHAUSTED;

@Component
public class AppWebFilterError implements WebFilter {
    @Override
    public @NotNull Mono<Void> filter(final @NotNull ServerWebExchange exchange, final WebFilterChain chain) {
        return chain.filter(exchange).onErrorResume(throwable -> {
            if (throwable instanceof final WebExchangeBindException exception) {
                return getAppErrorFromValidationsException(exception);
            }

            if (throwable instanceof final ServerWebInputException exception) {
                return getAppErrorFromWrongWebInputException(exception);
            }

            if (Exceptions.isRetryExhausted(throwable)) {
                return Mono.error(RETRY_EXHAUSTED.toException());
            }

            return getDefaultUnknownException(throwable);
        });
    }

    @NotNull
    private static Mono<Void> getDefaultUnknownException(final Throwable throwable) {
        return Mono.error(
                AppErrorCatalog.INTERNAL_SERVER_ERROR.toException(List.of(throwable.getMessage()))
        );
    }

    @NotNull
    private static Mono<Void> getAppErrorFromWrongWebInputException(final ServerWebInputException exception) {
        return Mono.error(AppErrorCatalog.BAD_REQUEST.toException(List.of(exception.getCause().getMessage())));
    }

    @NotNull
    private static Mono<Void> getAppErrorFromValidationsException(final WebExchangeBindException exception) {
        return Mono.error(AppErrorCatalog.BAD_REQUEST.toException(exception.getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .toList()));
    }

}
