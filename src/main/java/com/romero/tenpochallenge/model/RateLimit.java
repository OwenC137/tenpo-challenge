package com.romero.tenpochallenge.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RateLimit {
    private Integer limit;
    private Integer remaining;
    private Long expiration;
}
