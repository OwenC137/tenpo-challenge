package com.romero.tenpochallenge.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table
public class RequestData {
    @Id
    private Long id;
    @Column("body")
    private String body;
    @Column("response")
    private String response;
    @Column("timestamp")
    private LocalDateTime date;
}
