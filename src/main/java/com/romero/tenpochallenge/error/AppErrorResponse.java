package com.romero.tenpochallenge.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppErrorResponse {
    private String traceId;
    private OffsetDateTime timestamp;
    private String errorCode;
    private List<String> messages;
}
