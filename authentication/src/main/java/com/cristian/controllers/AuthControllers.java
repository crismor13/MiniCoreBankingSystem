package com.cristian.controllers;

import com.cristian.security.TokenUtils;
import com.cristian.user.UserDTO;
import com.cristian.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthControllers {

    @Autowired
    UserService userService;

    @Autowired
    TokenUtils tokenUtils;

    @GetMapping("/hi")
    public String sayHello(HttpSession session) {
        return "Hello world";
    }
    @GetMapping("/bye")
    public String sayGoodBye(HttpSession session) {
        return "See you later";
    }

    @PostMapping("/register")
    public void saveUser(@RequestBody UserDTO userDTO) {
        userService.saveUser(userDTO);
    }

    @PostMapping("/validateToken")
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        log.info("Trying to validate token {}", token);
        return ResponseEntity.ok(tokenUtils.validateToken(token));
    }

}
