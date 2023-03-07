package com.paymybuddy.transactionapp.service;

import com.paymybuddy.transactionapp.dto.RegisterDto;
import com.paymybuddy.transactionapp.entity.UserAccount;
import com.paymybuddy.transactionapp.repository.UserAccountRepository;
import com.paymybuddy.transactionapp.service.Impl.UserAccountServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserAccountServiceImpl userAccountService;



    @SneakyThrows
    @Test
    @DisplayName("UserService calls createUserAccount method and returns CreateUserDto from userAccount")
    public void TestCreateUser(){
        // GIVEN
        List<UserAccount> connectionList = new ArrayList<>();
        UserAccount userAccount = new UserAccount(null,"email@email","username","password",BigDecimal.ZERO,connectionList);
        RegisterDto registerDto = new RegisterDto(userAccount.getEmail(),userAccount.getPassword(),userAccount.getUsername());

        //UserAccount newUser =
        // WHEN
        when(userAccountRepository.findByEmail(registerDto.getEmail())).thenReturn(Optional.empty());
        String encodedPassword = "$2a$10$6b3VZMNBjdOgvHBEulqiCea.2HPaNl92GmmzNSKqwuBtStSI89S7O";
        when(passwordEncoder.encode(registerDto.getPassword())).thenReturn(encodedPassword);
        userAccount.setPassword(encodedPassword);
        when(userAccountRepository.save(userAccount)).thenReturn(userAccount);
        UserAccount result = userAccountService.createUser(registerDto);
        // THEN
        //TODO:result = null ?
        assertThat(result).isEqualTo(userAccount);
        verify(userAccountRepository,times(1)).save(userAccount);
    }
    @SneakyThrows
    @Test
    @DisplayName("UserService calls addConnection method with connection's email")
    public void TestAddConnection(){
        // GIVEN
        List<UserAccount> connectionList = new ArrayList<>();
        UserAccount userAccount = new UserAccount(1L,"user@example.com","user","user",null,connectionList);
        UserAccount connectionAccount = new UserAccount(2L,"user2@example.com","user2","user2",null,null);

        // WHEN
        //TODO: on peut pas mocker une mehode dans l'autre méthodde de userAccountService?
        when(userAccountRepository.findByEmail(connectionAccount.getEmail())).thenReturn(Optional.of(connectionAccount));
        //TODO: getConnectedUser -> authentication?
        userAccountService.addFriend(connectionAccount.getEmail());
        // THEN
        verify(userAccountRepository,times(1)).save(userAccount);
    }
   @Test
    @DisplayName("User can debit amount to his bank account")
    public void TestDebitBalanceAmountToBank(){
        // GIVEN

        UserAccount userAccount = new UserAccount(1L,"user@example.com","user","user",BigDecimal.valueOf(50),null);
        BigDecimal actualBalance = userAccount.getBalance();
        BigDecimal amount = BigDecimal.valueOf(30);
        actualBalance.subtract(amount);
        userAccount.setBalance(actualBalance);
        // WHEN
        //TODO: authnticationNotFoundException
        userAccountService.debitBalance(amount);
        // THEN
            verify(userAccount.getBalance()).equals(BigDecimal.valueOf(20));
    }
    @Test
    @DisplayName("UserService calls addFriend method with friendId")
    public void TestCreditBalance(){
        // GIVEN
        UserAccount userAccount = new UserAccount(1L,"user@example.com","user","user",BigDecimal.ZERO,null);
        BigDecimal actualBalance = userAccount.getBalance();
        BigDecimal amount = BigDecimal.valueOf(50);
        actualBalance = actualBalance.add(amount);
        userAccount.setBalance(actualBalance);
        // WHEN
        //TODO: authenticationNotFoundException !
        userAccountService.creditBalance(amount);
        // THEN
        verify(userAccountRepository,times(1)).save(userAccount);
    }


}
