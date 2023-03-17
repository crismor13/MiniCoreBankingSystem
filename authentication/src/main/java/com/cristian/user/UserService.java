package com.cristian.user;

import com.cristian.enums.AccountType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface UserService {

    void getUser(Long id);
    void saveUser(UserDTO userDTO);
    void deleteUser(Long id);

    void addAccount(AccountType accountType, UserEntity userEntity);
//    UserEntity updateUser(Long id);



}
