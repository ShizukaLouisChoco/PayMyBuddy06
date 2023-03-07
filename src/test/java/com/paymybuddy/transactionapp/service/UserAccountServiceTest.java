package com.paymybuddy.transactionapp.service;

import com.paymybuddy.transactionapp.dto.RegisterDto;
import com.paymybuddy.transactionapp.entity.UserAccount;
import com.paymybuddy.transactionapp.repository.UserAccountRepository;
import com.paymybuddy.transactionapp.service.Impl.UserAccountServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    @Mock
    private ConnectedUserDetailsService connectedUserDetailsService;

    @MockBean
    private UserAccountServiceImpl userAccountService;




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
    public void TestAddConnectionThrowsException(){
        // GIVEN
        // WHEN
        // THEN
    }



    @Test
    @DisplayName("connected user can add friend who is not in his contact list")
    public void TestAddFriend(){
        // GIVEN
        List<UserAccount> connectionList = new ArrayList<>();
        UserAccount friendUserAccount = new UserAccount(2L,"user2@example.com","user2","user2",null,connectionList);
        UserAccount connectedUserAccount = new UserAccount(1L,"user@example.com","user","user",null,connectionList);
        //userAccountServiceMock.createUser(new RegisterDto(connectedUserAccount.getEmail(),connectedUserAccount.getPassword(),connectedUserAccount.getUsername()));
        //userAccountServiceMock.createUser(new RegisterDto(friendUserAccount.getEmail(),friendUserAccount.getPassword(),friendUserAccount.getUsername()));
        when(connectedUserDetailsService.getConnectedUser()).thenReturn(connectedUserAccount);
        when(userAccountService.getUser(friendUserAccount.getEmail())).thenReturn(friendUserAccount);

        // WHEN
        userAccountService.addFriend(friendUserAccount.getEmail());
        // THEN
        verify(userAccountRepository,times(1)).save(connectedUserAccount);
    }

    @Test
    @DisplayName("connected user cannot add friend already in his contact list")
    public void TestAddFriendThrowsFriendExistsException(){

    }

    @Test
    @DisplayName("connected user cannot add himself in his contact list")
    public void TestAddFriendThrowsYouCannotAddYourselfException(){

    }

    @Test
    @DisplayName("getUser method can find UserAccount from email")
    public void testGetUser() {

    }

    @Test
    @DisplayName("getUser method throws exception with email not exists")
    public void testGetUserThrowsException() {

    }

    @Test
    @DisplayName("In case of identified user, method will correctely returned userAccount")
    public void testGetConnectedUser() {
    }

    @Test
    @DisplayName("In case non identified, method will throw AuthenticationCredentialsNotFoundException")
    public void testGetConnectedUserThrowsException() {
    }

    @Test
    @DisplayName("User can debit amount to his bank account")
    public void TestDebitBalance(){
        // GIVEN
        UserAccount userAccount = new UserAccount(1L,"user@example.com","user","user",BigDecimal.valueOf(50),null);
        BigDecimal actualBalance = userAccount.getBalance();
        BigDecimal amount = BigDecimal.valueOf(30);
        actualBalance.subtract(amount);
        userAccount.setBalance(actualBalance);
        when(connectedUserDetailsService.getConnectedUser()).thenReturn(userAccount);
        // WHEN
        //TODO: authnticationNotFoundException
        userAccountService.debitBalance(amount);
        // THEN
            verify(userAccount.getBalance()).equals(BigDecimal.valueOf(20));
    }

    @Test
    @DisplayName("method will throw exception if the balance is not enough")
    public void TestDebitBalanceThrowsException(){

    }
    @Test
    @DisplayName("user can credit balance")
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

    @Test
    @DisplayName("User can update UserAccount")
    public void TestUpdate(){

    }

    @Test
    @DisplayName("method will throw exception if user doesn't exist")
    public void TestUpdateThrowsException(){

    }

    @Test
    @DisplayName("method can find UserAccount by UserAccount Id")
    public void TestGetUserById(){

    }

    @Test
    @DisplayName("method will throw exception if userId doesn't exist")
    public void TestGetUserByIdThrowsException(){

    }
}
