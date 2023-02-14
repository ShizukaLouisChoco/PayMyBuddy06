package com.paymybuddy.transactionapp.service;

import com.paymybuddy.transactionapp.dto.CreateUserDto;
import com.paymybuddy.transactionapp.dto.UserAccountDto;
import com.paymybuddy.transactionapp.entity.UserAccount;

import java.math.BigDecimal;

public interface UserAccountService {

    //create
    CreateUserDto createUser(UserAccount user);

    //update
    UserAccount addConnection(String connectionEmail, UserAccount userAccount);

    //read
    UserAccountDto findByEmail(String email);

    //delete
    void deleteUserAccount(UserAccount userAccount);
    void deleteConnection(String connectionEmail);

    //function
    void debitBalance(BigDecimal amount);
    void creditBalance(BigDecimal amount);

}
