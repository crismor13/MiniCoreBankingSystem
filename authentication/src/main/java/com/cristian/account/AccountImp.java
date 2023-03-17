package com.cristian.account;
import com.cristian.enums.AccountStatus;
import com.cristian.enums.AccountType;
import com.cristian.user.TransactionDTO;
import com.cristian.user.TransactionStatus;
import com.cristian.user.UserEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class AccountImp implements AccountService {

    private final AccountRepository accountRepository;


    @Override
    public String accountNumber() {
        return String.format("%012d", Math.abs(UUID.randomUUID().getLeastSignificantBits() % 1000000000000L));
    }

    @Override
    public void saveAccount(AccountEntity accountEntity) {
        accountEntity.setNumber(accountNumber());
        accountRepository.save(accountEntity);
    }

    @Override
    @Transactional
    public TransactionDTO makeTransaction(TransactionDTO transactionRequest) {



        String receiverNumber = transactionRequest.getOrigin();
        String senderNumber = transactionRequest.getDestination();
        BigDecimal amount = transactionRequest.getQuantity();

        Optional<AccountEntity> receiverAccount = accountRepository.findByNumber(receiverNumber);
        Optional<AccountEntity> senderAccount = accountRepository.findByNumber(senderNumber);
        if (receiverAccount.isEmpty() || receiverAccount.get().getStatus() != AccountStatus.ACTIVE) {
            System.out.println("Receiver account does not exist or not active");
            log.info("Receiver account does not exist or not active");
            return transactionRequest;
        } else if (amount.compareTo(senderAccount.get().getBalance()) > 0) {
            System.out.println("Sender does not have enough balance");
            log.info("Sender does not have enough balance");
            return transactionRequest;
        } else {

            AccountEntity senderToUpdate = accountRepository.findByNumber(senderNumber).get();
            AccountEntity receiverToUpdate = accountRepository.findByNumber(receiverNumber).get();

            //The transaction

            BigDecimal newSenderBalance = senderToUpdate.getBalance().subtract(amount);
            senderToUpdate.setBalance(newSenderBalance);

            BigDecimal newReceiverBalance = receiverToUpdate.getBalance().add(amount);
            receiverToUpdate.setBalance(newReceiverBalance);

            accountRepository.save(senderToUpdate);
            accountRepository.save(receiverToUpdate);

            transactionRequest.setStatus(TransactionStatus.ACCEPTED);
            transactionRequest.setDate(LocalDateTime.now());

            return transactionRequest;

        }
    }

    //Used to add new account to existing user
//    @Override
//    public ResponseEntity<?> createAndSaveAccount(AccountType accountType, UserEntity user) {
//
//        for (AccountType type : AccountType.values()) {
//            if (type.name().equalsIgnoreCase(accountType.name())) {
//
//                AccountEntity newAccount = new AccountEntity(accountType, user);
//                newAccount.setNumber(accountNumber());
//
//                try {
//                    AccountEntity response = accountRepository.save(newAccount);
//                    return  new ResponseEntity(response, HttpStatus.CREATED);
//                } catch (Exception e) {
//                    return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
//                }
//            }
//        }
//        return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
//
//    }


}
