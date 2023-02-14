package com.paymybuddy.transactionapp.service;

import com.paymybuddy.transactionapp.dto.CreateUserDto;
import com.paymybuddy.transactionapp.entity.Balance;
import com.paymybuddy.transactionapp.entity.Transaction;
import com.paymybuddy.transactionapp.entity.UserAccount;
import com.paymybuddy.transactionapp.repository.UserAccountRepository;
import com.paymybuddy.transactionapp.service.Impl.UserAccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserAccountServiceTest {

    @Mock
    private UserAccountRepository userAccountRepository;


    private UserAccountService userService;


    @BeforeEach
    public void setup() {
        this.userService = new UserAccountServiceImpl(userAccountRepository);

    }

    @Test
    @DisplayName("UserService calls createUserAccount method and returns CreateUserDto from userAccount")
    public void TestCreateUser(){
        // GIVEN
        UserAccount userAccount = new UserAccount(UUID.randomUUID(),"email@email","username","newPassword",null,null);
        CreateUserDto expectedResult = new CreateUserDto(userAccount);
        // WHEN
        var result = userService.createUser(userAccount);
        // THEN
        verify(userAccountRepository,times(1)).save(userAccount);
        assertThat(result).isEqualTo(expectedResult);
    }
    @Test
    @DisplayName("UserService calls addFriend method with friendId")
    public void TestAddFriend(){
        // GIVEN
        UserAccount userAccount = new UserAccount(UUID.randomUUID(),"email@email","username","newPassword",null,null);
        UserAccount connectionAccount = new UserAccount(UUID.randomUUID(),"connection@email","connectionname","newPassword",null,null);

        // WHEN
        userService.addConnection(connectionAccount.getEmail(),userAccount );
        // THEN
        verify(userAccountRepository,times(1)).save(userAccount);
    }
   @Test
    @DisplayName("User can debit amount to his bank account")
    public void TestDebitBalanceAmountToBank(){
        // GIVEN
        UserAccount userAccount = new UserAccount(UUID.randomUUID(),"creditor@email","creditor","newPassword",null,null);
        Balance balance = new Balance(UUID.randomUUID(),userAccount,null,null,null,null);
        userAccount.setBalance(balance);
        BigDecimal amount = BigDecimal.valueOf(50);
        // WHEN
        userService.debitBalanceAmountToBank(amount,userAccount);
        // THEN
            verify(userAccount.getBalance()).equals(BigDecimal.valueOf(20));
    }
    @Test
    @DisplayName("UserService calls addFriend method with friendId")
    public void TestCreditBalanceAmountFromBank(){
        // GIVEN
        UserAccount userAccount = new UserAccount(UUID.randomUUID(),"user@email","user","pass123",null,null);
        Balance balance = new Balance(UUID.randomUUID(),userAccount,null,null,null,null);
        userAccount.setBalance(balance);
        BigDecimal amount = BigDecimal.valueOf(50);
        // WHEN
        userService.creditBalanceAmountFromBank(amount,userAccount);
        // THEN
      //  verify(userAccountRepository,times(1)).save(balance);
    }


}
