package com.cristian.user;

import com.cristian.account.AccountEntity;
import com.cristian.enums.AccountType;
import jakarta.persistence.*;

import java.util.List;

public class UserDTO {
    public String firstName;
    public String lastName;
    public String email;
    public String password;
    public Integer identificationNumber;
    public Integer cellphoneNumber;
    @Enumerated(EnumType.STRING)
    public AccountType accountType;
}
