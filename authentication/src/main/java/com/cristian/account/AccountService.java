package com.cristian.account;

import com.cristian.enums.AccountType;
import com.cristian.user.TransactionDTO;
import com.cristian.user.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


public interface AccountService {
    String accountNumber();
//    ResponseEntity<?> createAndSaveAccount(AccountType accountType, UserEntity user);

    void saveAccount(AccountEntity accountEntity);
    TransactionDTO makeTransaction(TransactionDTO transactionRequest);
}
