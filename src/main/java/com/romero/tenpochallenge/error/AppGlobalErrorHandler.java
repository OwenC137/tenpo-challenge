package com.romero.tenpochallenge.error;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class AppGlobalErrorHandler extends AbstractErrorWebExceptionHandler {

    private final static String ERROR_ATTRIBUTE_TIMESTAMP = "timestamp";
    private final static String ERROR_ATTRIBUTE_TRACE_ID = "traceId";
    private final static String ERROR_ATTRIBUTE_STATUS = "status";
    private final static String ERROR_ATTRIBUTE_MESSAGES = "message";
    private final static String ERROR_ATTRIBUTE_ERROR_CODE = "errorCode";

    public AppGlobalErrorHandler(final ErrorAttributes errorAttributes,
                                 final ApplicationContext applicationContext,
                                 final ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, new WebProperties.Resources(), applicationContext);
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(final ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(final ServerRequest request) {

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        final Map<String, Object> errorPropertiesMap = this.getErrorAttributes(request, ErrorAttributeOptions.defaults());
        final Map<String, Object> overridenErrorPropertiesMap = new HashMap<>();
        overridenErrorPropertiesMap.put(ERROR_ATTRIBUTE_TIMESTAMP, errorPropertiesMap.get("timestamp"));
        overridenErrorPropertiesMap.put(ERROR_ATTRIBUTE_TRACE_ID, UUID.randomUUID().toString());
        overridenErrorPropertiesMap.put(ERROR_ATTRIBUTE_STATUS, httpStatus.value());
        overridenErrorPropertiesMap.put(ERROR_ATTRIBUTE_MESSAGES, List.of("Ups! Something went wrong."));
        overridenErrorPropertiesMap.put(ERROR_ATTRIBUTE_ERROR_CODE, "INTERNAL_SERVER_ERROR");


        final Throwable throwable = this.getError(request);
        if (throwable instanceof final AppException appException) {
            final AppErrorCatalog errorCatalog = appException.getErrorCatalog();
            overridenErrorPropertiesMap.put(ERROR_ATTRIBUTE_STATUS, errorCatalog.getStatus().value());
            overridenErrorPropertiesMap.put(ERROR_ATTRIBUTE_MESSAGES, appException.getMessages().isEmpty() ?
                    List.of(errorCatalog.getDescription()) :
                    appException.getMessages());
            overridenErrorPropertiesMap.put(ERROR_ATTRIBUTE_ERROR_CODE, errorCatalog.getCode());
            httpStatus = errorCatalog.getStatus();
        }

        return ServerResponse.status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(overridenErrorPropertiesMap));
    }
}
