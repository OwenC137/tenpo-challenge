package com.romero.tenpochallenge.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Builder
@Data
public class SumAndAddPercentageRequest {
    @NotNull
    @Min(0)
    private final BigDecimal firstNumber;
    @NotNull
    @Min(0)
    private final BigDecimal secondNumber;
}
