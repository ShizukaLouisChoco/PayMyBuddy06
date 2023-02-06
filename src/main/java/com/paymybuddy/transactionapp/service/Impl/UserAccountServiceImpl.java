package com.paymybuddy.transactionapp.service.Impl;

import com.paymybuddy.transactionapp.dto.CreateUserDto;
import com.paymybuddy.transactionapp.dto.UserAccountDto;
import com.paymybuddy.transactionapp.entity.Balance;
import com.paymybuddy.transactionapp.exception.UserAccountNotFoundException;
import com.paymybuddy.transactionapp.entity.UserAccount;
import com.paymybuddy.transactionapp.repository.BalanceRepository;
import com.paymybuddy.transactionapp.repository.UserAccountRepository;
import com.paymybuddy.transactionapp.service.UserAccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    private final static Logger logger = LogManager.getLogger("UserAccountServiceImpl");

    private final UserAccountRepository userAccountRepository;
    private final BalanceRepository balanceRepository;

    public UserAccountServiceImpl(UserAccountRepository userAccountRepository, BalanceRepository balanceRepository) {
        this.userAccountRepository = userAccountRepository;
        this.balanceRepository = balanceRepository;
    }


    //create
    @Override
    public CreateUserDto createUser(UserAccount user){
        userAccountRepository.save(user);
        return new CreateUserDto(user);
    };

    //update
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

    @Override
    public void deleteConnection(String connectionEmail) {

    }

    //function
    @Override
    public void debitBalanceAmountToBank(BigDecimal amount, UserAccount userAccount) {
        Balance userBalance = userAccount.getBalance();
        BigDecimal actualBalance =userBalance.getUserBalance();
        actualBalance.subtract(amount);
        userBalance.setUserBalance(actualBalance);
        balanceRepository.save(userBalance);
    }

    @Override
    public void creditBalanceAmountFromBank(BigDecimal amount, UserAccount userAccount) {
        Balance userBalance = userAccount.getBalance();
        BigDecimal actualBalance =userBalance.getUserBalance();
        actualBalance.add(amount);
        userBalance.setUserBalance(actualBalance);
        balanceRepository.save(userBalance);

    }


}
