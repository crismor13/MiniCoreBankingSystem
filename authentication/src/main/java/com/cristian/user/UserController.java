package com.cristian.user;

import com.cristian.account.AccountEntity;
import com.cristian.account.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {


    private final UserService userService;
    private final AccountService accountService;
    private final KafkaTemplate<String, String> kafkaTemplate;


    public UserController (KafkaTemplate<String, String> kafkaTemplate,UserService userService, AccountService accountService) {
        this.kafkaTemplate = kafkaTemplate;
        this.userService = userService;
        this.accountService = accountService;
    }

    @PostMapping()
    public void saveUser (@RequestBody UserDTO userDTO){

        userService.saveUser(userDTO);
    }

    @PostMapping(path = "/transactions")
    public TransactionDTO tryTransaction (@RequestBody TransactionDTO transactionDTO) {
        return accountService.makeTransaction(transactionDTO);
    }

    @DeleteMapping( path = "/{id}")
    public void deleteUser (@PathVariable("id") Long id){
        userService.deleteUser(id);
    }

    @GetMapping()
    public String sayHello(HttpSession session) {
        return "Hi from users" + session.getId();
    }

}
