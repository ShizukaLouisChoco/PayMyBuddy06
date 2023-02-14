package com.paymybuddy.transactionapp.service;

import com.paymybuddy.transactionapp.dto.RegisterDto;
import com.paymybuddy.transactionapp.entity.UserAccount;
import com.paymybuddy.transactionapp.repository.UserAccountRepository;
import com.paymybuddy.transactionapp.service.Impl.UserAccountServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @SneakyThrows
    @Test
    @DisplayName("UserService calls createUserAccount method and returns CreateUserDto from userAccount")
    public void TestCreateUser(){
        // GIVEN
        UserAccount userAccount = new UserAccount(Long.valueOf(001),"email@email","username","newPassword",null,null);
        RegisterDto registerDto = new RegisterDto(userAccount.getEmail(),userAccount.getPassword(),userAccount.getUsername());
        // WHEN
        var result = userService.createUser(registerDto);
        // THEN
        verify(userAccountRepository,times(1)).save(userAccount);
        assertThat(result).isEqualTo(userAccount);
    }
    @SneakyThrows
    @Test
    @DisplayName("UserService calls addConnection method with connection's id")
    public void TestAddConnection(){
        // GIVEN
        List<UserAccount> connectionList = new ArrayList<>();
        UserAccount userAccount = new UserAccount(Long.valueOf(001),"email@email","username","newPassword",null,connectionList);
        UserAccount connectionAccount = new UserAccount(Long.valueOf(002),"connection@email","connectionname","newPassword",null,null);

        // WHEN
        when(userAccountRepository.findByEmail(connectionAccount.getEmail())).thenReturn(Optional.of(connectionAccount));
        userService.addFriend(connectionAccount.getEmail());
        // THEN
        verify(userAccountRepository,times(1)).save(userAccount);
    }
   @Test
    @DisplayName("User can debit amount to his bank account")
    public void TestDebitBalanceAmountToBank(){
        // GIVEN

        UserAccount userAccount = new UserAccount(Long.valueOf(001),"creditor@email","creditor","newPassword",BigDecimal.valueOf(50),null);
        userAccount.setBalance(userAccount.getBalance());
        BigDecimal amount = BigDecimal.valueOf(30);
        // WHEN
        userService.debitBalance(amount);
        // THEN
            verify(userAccount.getBalance()).equals(BigDecimal.valueOf(20));
    }
    @Test
    @DisplayName("UserService calls addFriend method with friendId")
    public void TestCreditBalanceAmountFromBank(){
        // GIVEN
        UserAccount userAccount = new UserAccount(Long.valueOf(001),"user@email","user","pass123",null,null);
        BigDecimal balance = BigDecimal.valueOf(0);
        userAccount.setBalance(balance);
        BigDecimal amount = BigDecimal.valueOf(50);
        // WHEN
        userService.creditBalance(amount);
        // THEN
      //  verify(userAccountRepository,times(1)).save(balance);
    }


}
