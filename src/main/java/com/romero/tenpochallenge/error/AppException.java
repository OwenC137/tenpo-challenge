package com.romero.tenpochallenge.error;

import lombok.Getter;

import java.util.List;

@Getter
public class AppException extends RuntimeException {

    private final AppErrorCatalog errorCatalog;
    private List<String> messages = List.of();

    public AppException(final AppErrorCatalog errorCatalog) {
        super(errorCatalog.getDescription());
        this.errorCatalog = errorCatalog;
    }

    public AppException(final AppErrorCatalog errorCatalog, final List<String> messages) {
        super(errorCatalog.getDescription());
        this.errorCatalog = errorCatalog;
        this.messages = messages;
    }
}
