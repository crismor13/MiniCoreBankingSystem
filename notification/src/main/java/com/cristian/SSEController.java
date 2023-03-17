package com.cristian;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@RequestMapping("/notifications")
public class SSEController {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @GetMapping("/stream/{id}")
    public SseEmitter stream(@PathVariable String id) {
        log.info(id);
        SseEmitter emitter = new SseEmitter(-1L);
        emitters.put(id, emitter);
        emitter.onCompletion(() -> emitters.remove(id));
        emitter.onTimeout(() -> emitters.remove(id));
        return emitter;
    }

    @GetMapping("/hi")
    public String sayHi () {
        return "What's up?";
    }


    @PostMapping("/publish")
    public void publish(@RequestBody MessageDTO message) {
        SseEmitter emitter = emitters.get(message.getId());
        if (emitter != null) {
            try {
                emitter.send(message.getMessage());
            } catch (IOException e) {
                emitter.complete();
                emitters.remove(message.getId());
            }
        }
    }
}
