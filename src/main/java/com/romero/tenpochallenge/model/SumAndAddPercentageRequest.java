package com.romero.tenpochallenge.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class SumAndAddPercentageRequest {
    @NotNull
    @Min(0)
    private BigDecimal firstNumber;
    @NotNull
    @Min(0)
    private BigDecimal secondNumber;
}
