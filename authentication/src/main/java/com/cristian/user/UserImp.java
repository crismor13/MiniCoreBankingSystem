package com.cristian.user;

import com.cristian.account.AccountEntity;
import com.cristian.account.AccountService;
import com.cristian.enums.AccountType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserImp implements UserService{

    private final UserRepository userRepository;
    private final AccountService accountService;

    @Override
    public void getUser(Long id) {
        userRepository.findById(id);
    }

    @Override
    public void saveUser(UserDTO userDTO) {

        UserEntity newUserEntity = UserEntity.builder()
                .firstName(userDTO.firstName)
                .lastName(userDTO.lastName)
                .email(userDTO.email)
                .password(new BCryptPasswordEncoder().encode(userDTO.password))
                .identificationNumber(userDTO.identificationNumber)
                .cellphoneNumber(userDTO.cellphoneNumber)
                .build();

        List<AccountEntity> accountsList = new ArrayList<>();
        AccountEntity newAccount = new AccountEntity(userDTO.accountType, newUserEntity);
        accountsList.add(newAccount);
        newUserEntity.setAccounts(accountsList);
        userRepository.save(newUserEntity);
        accountService.saveAccount(newAccount);
        //        List<AccountEntity> accountsList = new ArrayList<>();
        //        AccountEntity newAccount = new AccountEntity();
        //        newAccount.setType(userDTO.accountType);
        //        newAccount.setNumber(accountService.accountNumber());
        //
        //        accountsList.add(newAccount);
        //
        //        UserEntity newUserEntity = UserEntity.builder()
        //                .firstName(userDTO.firstName)
        //                .lastName(userDTO.lastName)
        //                .email(userDTO.email)
        //                .identificationNumber(userDTO.identificationNumber)
        //                .cellphoneNumber(userDTO.cellphoneNumber)
        ////                .accounts(accountsList)
        //                .build();
        //
        //        newAccount.setUser(newUserEntity);
        //        userRepository.save(newUserEntity);

    }


    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void addAccount(AccountType accountType, UserEntity userEntity){
        Optional<UserEntity> checkUser = userRepository.findById(userEntity.getId());
        if (checkUser.isEmpty()){
            System.out.println("The user does not exist");
        } else {
            UserEntity user = userRepository.findById(userEntity.getId()).get();
            AccountEntity newAccount = new AccountEntity(accountType, userEntity);
            List<AccountEntity> userAccounts = user.getAccounts();
            userAccounts.add(newAccount);
            user.setAccounts(userAccounts);
            userRepository.save(user);

        }
    }

//    @Override
//    public UserEntity updateUser(Long id)
}
