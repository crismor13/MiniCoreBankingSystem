package com.cristian.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity,Long> {
    Optional<AccountEntity> findByNumber (String number);
}
