package com.romero.tenpochallenge.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@AllArgsConstructor
public enum AppErrorCatalog {
    BAD_REQUEST("BAD_REQUEST", "Bad Request", HttpStatus.BAD_REQUEST),
    RATE_LIMIT_EXCEEDED("RATE_LIMIT_EXCEEDED", "You have exhausted your API Request Quota", HttpStatus.TOO_MANY_REQUESTS),
    RETRY_EXHAUSTED("RETRY_EXHAUSTED", "Retry Exhausted", HttpStatus.INTERNAL_SERVER_ERROR),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String description;
    private final HttpStatus status;

    public AppException toException() {
        return new AppException(this);
    }

    public AppException toException(final List<String> messages) {
        return new AppException(this, messages);
    }
}
