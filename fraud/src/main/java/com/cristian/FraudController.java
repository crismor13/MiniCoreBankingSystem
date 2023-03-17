package com.cristian;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@RequestMapping("/fraud")
public class FraudController {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @PostMapping("/isFraudster")
    public Boolean stream(@RequestBody TransactionDTO transactionDTO) {
        //some fraud detection logic
        return false;
    }

    @GetMapping("/hi")
    public String sayHi () {
        return "What's up?";
    }

}
