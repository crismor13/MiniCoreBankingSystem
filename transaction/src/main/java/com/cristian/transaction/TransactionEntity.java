package com.cristian.transaction;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String origin;

    private String destination;

    private BigDecimal quantity;

    private LocalDateTime date = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    public TransactionEntity(String origin, String destination, BigDecimal quantity, TransactionStatus status) {
        this.origin = origin;
        this.destination = destination;
        this.quantity = quantity;
        this.status = status;
    }
}
