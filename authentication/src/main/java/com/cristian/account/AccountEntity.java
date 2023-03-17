package com.cristian.account;

import com.cristian.enums.AccountStatus;
import com.cristian.enums.AccountType;
import com.cristian.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity

public class AccountEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String number;

    @Enumerated(EnumType.STRING)
    private AccountType type;


    @Enumerated(EnumType.STRING)
    private AccountStatus status = AccountStatus.ACTIVE;

    private BigDecimal balance = new BigDecimal(1000);

    private LocalDateTime createdAt = LocalDateTime.now();

    public AccountEntity(AccountType type, UserEntity user) {
        this.type = type;
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;



}
