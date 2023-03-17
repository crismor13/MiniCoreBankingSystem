package com.cristian.user;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionDTO {

    private String origin;

    private String destination;

    private BigDecimal quantity;

    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
}
