package com.romero.tenpochallenge.usecase.request;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestDataDto {
    private Long id;
    private JsonNode body;
    private JsonNode response;
    private LocalDateTime date;
}
