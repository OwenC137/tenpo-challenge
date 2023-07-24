package com.romero.tenpochallenge.usecase.calculator.operation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SumAndAddPercentageOperation {
    BigDecimal sum;
    BigDecimal percentage;
    BigDecimal result;
}
