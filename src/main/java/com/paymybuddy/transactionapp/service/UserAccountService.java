package com.paymybuddy.transactionapp.service;

import com.paymybuddy.transactionapp.dto.RegisterDto;
import com.paymybuddy.transactionapp.entity.UserAccount;
import com.paymybuddy.transactionapp.exception.EmailAlradyExistException;
import com.paymybuddy.transactionapp.exception.FriendAlreadyExistException;

import java.math.BigDecimal;

public interface UserAccountService {

    UserAccount createUser(RegisterDto user) throws EmailAlradyExistException;

    UserAccount addFriend(String friendEmail) throws FriendAlreadyExistException;

    UserAccount getUser(String email);

    UserAccount getConnectedUser();

    //function
    void debitBalance(BigDecimal amount);
    void creditBalance(BigDecimal amount);
    UserAccount update(UserAccount userAccountWithNewInfo);

}
