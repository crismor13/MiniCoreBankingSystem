package com.cristian.transaction;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service

public class TransactionImp implements TransactionService{


    private WebClient AuthorizationWebClient;
    private WebClient FraudWebClient;
    private final TransactionRepository transactionRepository;

    public TransactionImp(WebClient.Builder webclientBuilder, TransactionRepository transactionRepository) {

        this.FraudWebClient= webclientBuilder
                .baseUrl("http://FRAUD-SERVICE/fraud")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();

        this.AuthorizationWebClient = webclientBuilder
                .baseUrl("http://AUTHENTICATION-SERVICE/users") //account microservice url
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.transactionRepository = transactionRepository;
    }
//    ClientResponse response = webClient.post()
//            .uri("/accounts")
//            .body(Mono.just(request), ClientRequest.class)
//            .retrieve()
//            .bodyToMono(ClientResponse.class)
//            .block();

    @Override
    public Mono<TransactionDTO> createAndSaveTransaction (TransactionDTO transactionDTO) {

        TransactionEntity newTransaction = new TransactionEntity(
                transactionDTO.getOrigin(),
                transactionDTO.getDestination(),
                transactionDTO.getQuantity(),
                transactionDTO.getStatus()
        );

        transactionRepository.save(newTransaction);
        return Mono.just(transactionDTO);
    }

    @Override
    public Mono<TransactionDTO> transactionRequest(TransactionDTO transactionRequest, String token) throws Exception {

        Mono<Boolean> isTransactionFraudulent = FraudWebClient.post()
                .uri("/isFraudster")
                .body(Mono.just(transactionRequest), TransactionDTO.class)
                .retrieve()
                .bodyToMono(Boolean.class);

        if (isTransactionFraudulent.block()) {
            transactionRequest.setDate(LocalDateTime.now());
            Mono<TransactionDTO> declainedTransaction = Mono.just(transactionRequest);
            return declainedTransaction;
        }

        this.AuthorizationWebClient =  AuthorizationWebClient.mutate()
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();



        Mono<TransactionDTO> response =  AuthorizationWebClient.post()
                .uri("/transactions")
                .body(Mono.just(transactionRequest), TransactionDTO.class)
                .retrieve()
                .bodyToMono(TransactionDTO.class)
                .flatMap(subject -> createAndSaveTransaction(subject));

        return response;
    }

    //To do: If
}
