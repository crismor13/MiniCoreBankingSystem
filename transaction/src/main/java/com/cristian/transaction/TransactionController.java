package com.cristian.transaction;

import jakarta.servlet.http.HttpSession;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping
    public Mono<TransactionDTO> newTransaction (@RequestBody TransactionDTO transactionDTO, @RequestHeader(value = "Authorization", required = true) String token) throws Exception {

        String bearerToken = null;
        if (token != null && token.startsWith("Bearer ")) {
            bearerToken = token.substring(7);
        }

        Mono<TransactionDTO> transactionResponse = transactionService.transactionRequest(transactionDTO, token);
        return transactionResponse;
    }


    @GetMapping()
    public String sayHello() {
        return "Hi from transactions";
    }

}
