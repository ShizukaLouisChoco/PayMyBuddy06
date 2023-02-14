package com.paymybuddy.transactionapp.service.Impl;

import com.paymybuddy.transactionapp.dto.CreateUserDto;
import com.paymybuddy.transactionapp.dto.UserAccountDto;
import com.paymybuddy.transactionapp.entity.Balance;
import com.paymybuddy.transactionapp.exception.UserAccountNotFoundException;
import com.paymybuddy.transactionapp.entity.UserAccount;
import com.paymybuddy.transactionapp.repository.UserAccountRepository;
import com.paymybuddy.transactionapp.service.UserAccountService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Service
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserAccountServiceImpl(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }


    @Override
    public UserAccount createUser(RegisterDto user) throws EmailAlradyExistException{
        //verification if the email is already used
        Optional<UserAccount> userExists = userAccountRepository.findByEmail(user.getEmail());

        if(userExists.isPresent()){
            throw new EmailAlradyExistException("Email exists");
        }
        //if it's not used, save in database with encoded password
        UserAccount newUser = new UserAccount();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPassword( passwordEncoder.encode(user.getPassword()));

        return userAccountRepository.save(newUser);
    }


    @Override
    public UserAccount addConnection(String connectionEmail,UserAccount userAccount){
        Optional<UserAccount> optionalConnection = userAccountRepository.findByEmail(connectionEmail);
        UserAccount connection = optionalConnection.get();
        userAccount.addConnections(connection);
        return userAccountRepository.save(userAccount);
    }

    //read
    @Override
    public UserAccountDto findByEmail(String email) {
        userAccountRepository.findByEmail(email).orElseThrow(() -> new UserAccountNotFoundException("User not found with email = " + email));
        return new UserAccountDto();
    }


    //delete
    @Override
    public void deleteUserAccount(UserAccount userAccount) {

    }


    //function
    @Override
    public void debitBalance(BigDecimal amount) {
        UserAccount connectedUser = getConnectedUser();
        connectedUser.debitAmount(amount);
        userAccountRepository.save(connectedUser);
    }

    @Override
    public void creditBalance(BigDecimal amount) {
        UserAccount connectedUser = getConnectedUser();
        connectedUser.creditAmount(amount);
        userAccountRepository.save(connectedUser);

    }

}
