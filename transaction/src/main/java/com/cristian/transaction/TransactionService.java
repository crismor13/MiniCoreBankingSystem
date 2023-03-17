package com.cristian.transaction;

import reactor.core.publisher.Mono;

public interface TransactionService {

    Mono<TransactionDTO> transactionRequest(TransactionDTO transactionDTO, String token) throws Exception;
    Mono<TransactionDTO> createAndSaveTransaction(TransactionDTO transactionDTO);
}
