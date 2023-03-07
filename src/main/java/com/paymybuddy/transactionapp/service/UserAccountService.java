package com.paymybuddy.transactionapp.service;

import com.paymybuddy.transactionapp.dto.RegisterDto;
import com.paymybuddy.transactionapp.entity.UserAccount;
import com.paymybuddy.transactionapp.exception.EmailAlradyExistException;
import com.paymybuddy.transactionapp.exception.FriendAddingException;

import java.math.BigDecimal;

public interface UserAccountService {

    UserAccount createUser(RegisterDto user) throws EmailAlradyExistException;

    UserAccount addFriend(String friendEmail) throws FriendAddingException;

    UserAccount getUser(String email);

    //function
    void debitBalance(BigDecimal amount);
    void creditBalance(BigDecimal amount);
    UserAccount update(UserAccount userAccountWithNewInfo);

    UserAccount getUserById(Long creditorId);
}
