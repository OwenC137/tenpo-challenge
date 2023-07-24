package com.romero.tenpochallenge.usecase.calculator.percentage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Builder
@RedisHash
public class Percentage implements Serializable {
    @Serial
    @Id
    private static final long serialVersionUID = 1L;
    private BigDecimal value;
}
