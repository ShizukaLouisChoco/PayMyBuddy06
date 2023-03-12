package com.paymybuddy.transactionapp.service;

import com.paymybuddy.transactionapp.dto.RegisterDto;
import com.paymybuddy.transactionapp.entity.UserAccount;
import com.paymybuddy.transactionapp.exception.BalanceException;
import com.paymybuddy.transactionapp.exception.EmailAlradyExistException;
import com.paymybuddy.transactionapp.exception.FriendAddingException;
import com.paymybuddy.transactionapp.exception.UserAccountNotFoundException;
import com.paymybuddy.transactionapp.repository.UserAccountRepository;
import com.paymybuddy.transactionapp.service.Impl.UserAccountServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserAccountServiceTest {

    @InjectMocks
    private UserAccountServiceImpl userAccountService;
    @Mock
    private ConnectedUserDetailsService connectedUserDetailsService;
    @Mock
    private UserAccountRepository userAccountRepository;
    @Mock
    private PasswordEncoder passwordEncoder;




    @Test
    @DisplayName("createUser method can create new account")
    public void TestCreateUser() throws Exception {
        // GIVEN
        final List<UserAccount> connectionList = List.of();
        final RegisterDto registerDto = new RegisterDto("user@email.com", "password", "username");
        final String encodedPassword = "encodedPassword";

        final UserAccount expectedUserAccount = new UserAccount(null, registerDto.getEmail() ,registerDto.getUsername(),encodedPassword ,BigDecimal.ZERO, connectionList);

        // WHEN
        when(userAccountRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn(encodedPassword);
        when(userAccountRepository.save(any(UserAccount.class))).thenAnswer(r -> r.getArguments()[0]);

        UserAccount result = userAccountService.createUser(registerDto);

        // THEN
        verify(userAccountRepository,times(1)).save(any(UserAccount.class));

        assertThat(result)
                .isNotNull()
                .satisfies(arg -> assertThat(arg).isEqualTo(expectedUserAccount));
    }
    @Test
    @DisplayName("mail adress existant throws exception")
    public void TestCreateUserThrowsException(){
        // GIVEN
        List<UserAccount> connectionList = new ArrayList<>();
        RegisterDto registerDto = new RegisterDto("user@email.com", "password", "username");
        String encodedPassword = "encodedPassword";
        UserAccount registeredUserAccount = new UserAccount(null, registerDto.getEmail() ,registerDto.getUsername(),encodedPassword ,BigDecimal.ZERO, connectionList);

        // WHEN
        when(userAccountRepository.findByEmail(registeredUserAccount.getEmail())).thenReturn(Optional.of(registeredUserAccount));


        // THEN
            assertThatThrownBy(() -> {
                userAccountService.createUser(registerDto);
            })
                    .isInstanceOf(EmailAlradyExistException.class)
                    .hasMessageContaining("Your email address is already registered");
    }



    @Test
    @DisplayName("connected user can add friend who is not in his contact list")
    public void TestAddFriend(){
        // GIVEN
         List<UserAccount> connectionList = new ArrayList<>();
         RegisterDto registerDto = new RegisterDto("user@email.com", "password", "username");
         RegisterDto friendRegisterDto = new RegisterDto("user2@email.com", "password2", "username2");
         String encodedPassword = "encodedPassword";
         UserAccount connectedUserAccount = new UserAccount(null, registerDto.getEmail() ,registerDto.getUsername(),encodedPassword ,BigDecimal.ZERO, connectionList);
         UserAccount expectedUserAccount = new UserAccount(null, friendRegisterDto.getEmail() ,friendRegisterDto.getUsername(),encodedPassword ,BigDecimal.ZERO, connectionList);

        // WHEN
        when(userAccountRepository.findByEmail(connectedUserAccount.getEmail())).thenReturn(Optional.of(connectedUserAccount));
        when(userAccountRepository.findByEmail(expectedUserAccount.getEmail())).thenReturn(Optional.of(expectedUserAccount));
        when(connectedUserDetailsService.getConnectedUser()).thenReturn(connectedUserAccount);
        when(userAccountRepository.save(any(UserAccount.class))).thenAnswer(r -> r.getArguments()[0]);

        UserAccount result = userAccountService.addFriend(expectedUserAccount.getEmail());

        // THEN
        verify(userAccountRepository,times(1)).save(any(UserAccount.class));

        assertThat(result)
                .isNotNull()
                .satisfies(arg -> assertThat(arg).isEqualTo(connectedUserAccount),
                        arg -> assertThat(arg.getConnections()).contains(expectedUserAccount));

    }

    @Test
    @DisplayName("connected user cannot add friend already in his contact list")
    public void TestAddFriendThrowsFriendAddingException(){
        // GIVEN
        List<UserAccount> connectionList = new ArrayList<>();
        RegisterDto registerDto = new RegisterDto("user@email.com", "password", "username");
        RegisterDto friendRegisterDto = new RegisterDto("user2@email.com", "password2", "username2");
        String encodedPassword = "encodedPassword";
        UserAccount expectedUserAccount = new UserAccount(2L, friendRegisterDto.getEmail() ,friendRegisterDto.getUsername(),encodedPassword ,BigDecimal.ZERO, null);
        connectionList.add(expectedUserAccount);
        UserAccount connectedUserAccount = new UserAccount(1L, registerDto.getEmail() ,registerDto.getUsername(),encodedPassword ,BigDecimal.ZERO, connectionList);

        // WHEN
        when(userAccountRepository.findByEmail(connectedUserAccount.getEmail())).thenReturn(Optional.of(connectedUserAccount));
        when(userAccountRepository.findByEmail(expectedUserAccount.getEmail())).thenReturn(Optional.of(expectedUserAccount));
        when(connectedUserDetailsService.getConnectedUser()).thenReturn(connectedUserAccount);


        // THEN
      assertThatThrownBy(() -> {
                userAccountService.addFriend(expectedUserAccount.getEmail());
            })
                    .isInstanceOf(FriendAddingException.class)
                    .hasMessageContaining("This friend is already in your contact list");

    }

    @Test
    @DisplayName("connected user cannot add himself in his contact list")
    public void TestAddFriendThrowsYouCannotAddYourselfException(){
        // GIVEN
        List<UserAccount> connectionList = new ArrayList<>();
        RegisterDto registerDto = new RegisterDto("user@email.com", "password", "username");
        String encodedPassword = "encodedPassword";
        UserAccount connectedUserAccount = new UserAccount(1L, registerDto.getEmail() ,registerDto.getUsername(),encodedPassword ,BigDecimal.ZERO, connectionList);

        // WHEN
        when(userAccountRepository.findByEmail(connectedUserAccount.getEmail())).thenReturn(Optional.of(connectedUserAccount));
        when(connectedUserDetailsService.getConnectedUser()).thenReturn(connectedUserAccount);


        // THEN
            assertThatThrownBy(() -> {
                userAccountService.addFriend(connectedUserAccount.getEmail());
            })
                    .isInstanceOf(FriendAddingException.class)
                    .hasMessageContaining("You cannot add yourself in contact list");
    }

    @Test
    @DisplayName("getUser method can find UserAccount from id")
    public void testGetUserById() {
        // GIVEN
        final List<UserAccount> connectionList = List.of();
        final RegisterDto registerDto = new RegisterDto("user@email.com", "password", "username");
        final String encodedPassword = "encodedPassword";
        final UserAccount expectedUserAccount = new UserAccount(1L, registerDto.getEmail() ,registerDto.getUsername(),encodedPassword ,BigDecimal.ZERO, connectionList);

        // WHEN
        when(userAccountRepository.getReferenceById(any())).thenReturn(expectedUserAccount);

        UserAccount result = userAccountService.getUserById(expectedUserAccount.getId());

        // THEN
        assertThat(result)
                .isNotNull()
                .satisfies(arg -> assertThat(arg).isEqualTo(expectedUserAccount));

    }

    @Test
    @DisplayName("getUser throws exception with Email not found")
    public void testGetUserThrowsException() {
        // GIVEN
        final String email = "nonExistingEmail";

        // WHEN
        when(userAccountRepository.findByEmail(any())).thenReturn(Optional.empty());
        // THEN
        assertThatThrownBy(() -> {
            userAccountService.getUser(email);
        })
                .isInstanceOf(UserAccountNotFoundException.class)
                .hasMessageContaining("User not found with email = " + email );


    }

    @Test
    @DisplayName("User can debit amount to his bank account")
    public void TestDebitBalance(){
        // GIVEN
        UserAccount userAccount = new UserAccount(1L,"user@example.com","user","user",BigDecimal.valueOf(50),null);
        BigDecimal actualBalance = userAccount.getBalance();
        BigDecimal amount = BigDecimal.valueOf(30);
        BigDecimal expectedBalance = actualBalance.subtract(amount);
        // WHEN
        when(connectedUserDetailsService.getConnectedUser()).thenReturn(userAccount);
        when(userAccountRepository.save(any(UserAccount.class))).thenAnswer(r -> r.getArguments()[0]);

        userAccountService.debitBalance(amount);
        // THEN
        verify(userAccountRepository,times(1)).save(userAccount);
        assertThat(userAccount.getBalance()).isEqualTo(expectedBalance);

    }

    @Test
    @DisplayName("method will throw exception if the balance is not enough")
    public void TestDebitBalanceThrowsException(){
        List<UserAccount> connectionList = new ArrayList<>();
        RegisterDto registerDto = new RegisterDto("user@email.com", "password", "username");
        String encodedPassword = "encodedPassword";
        UserAccount connectedUserAccount = new UserAccount(1L, registerDto.getEmail() ,registerDto.getUsername(),encodedPassword ,BigDecimal.ZERO, connectionList);

        // WHEN
        when(userAccountRepository.findByEmail(connectedUserAccount.getEmail())).thenReturn(Optional.of(connectedUserAccount));
        when(connectedUserDetailsService.getConnectedUser()).thenReturn(connectedUserAccount);


        // THEN
        assertThatThrownBy(() -> {
            userAccountService.debitBalance(BigDecimal.valueOf(10));
        })
                .isInstanceOf(BalanceException.class)
                .hasMessageContaining("Your balance is not enough to transfer this amount");


    }
    @Test
    @DisplayName("user can credit balance")
    public void TestCreditBalance(){
        // GIVEN
        UserAccount userAccount = new UserAccount(1L,"user@example.com","user","user",BigDecimal.ZERO,null);
        BigDecimal actualBalance = userAccount.getBalance();
        BigDecimal amount = BigDecimal.valueOf(50);
        BigDecimal expectedBalance = actualBalance.add(amount);
        userAccount.setBalance(actualBalance);
        // WHEN
        when(connectedUserDetailsService.getConnectedUser()).thenReturn(userAccount);
        when(userAccountRepository.save(any(UserAccount.class))).thenAnswer(r -> r.getArguments()[0]);

        userAccountService.creditBalance(amount);
        // THEN
        verify(userAccountRepository,times(1)).save(userAccount);
        assertThat(userAccount.getBalance()).isEqualTo(expectedBalance);

    }

    @Test
    @DisplayName("User can update UserAccount")
    public void TestUpdate(){
// GIVEN
        RegisterDto registerDto = new RegisterDto("user@email.com", "password", "username");
        String encodedPassword = "encodedPassword";
        UserAccount connectedUserAccount = new UserAccount(1L, registerDto.getEmail() ,registerDto.getUsername(),encodedPassword ,BigDecimal.ZERO, null);
        UserAccount expectedUserAccount = new UserAccount(1L, registerDto.getEmail() ,"updatedUsername",encodedPassword ,BigDecimal.ZERO, null);
        // WHEN
        when(userAccountRepository.getReferenceById(connectedUserAccount.getId())).thenReturn(connectedUserAccount);
        when(userAccountRepository.save(any(UserAccount.class))).thenAnswer(r -> r.getArguments()[0]);

        UserAccount result = userAccountService.update(expectedUserAccount);

        // THEN
        verify(userAccountRepository,times(1)).save(any(UserAccount.class));

        assertThat(result)
                .isNotNull()
                .satisfies(arg -> assertThat(arg).isEqualTo(expectedUserAccount));

    }


}
