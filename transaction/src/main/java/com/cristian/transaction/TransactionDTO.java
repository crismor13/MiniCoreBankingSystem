package com.cristian.transaction;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TransactionDTO {

    private String origin;

    private String destination;

    private BigDecimal quantity;

    public TransactionDTO(String origin, String destination, BigDecimal quantity) {
        this.origin = origin;
        this.destination = destination;
        this.quantity = quantity;
    }

    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status = TransactionStatus.DECLINED;
}
