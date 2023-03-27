package com.paymybuddy.transactionapp.service;

import com.paymybuddy.transactionapp.entity.UserAccount;
import com.paymybuddy.transactionapp.exception.UserAccountNotFoundException;
import com.paymybuddy.transactionapp.repository.UserAccountRepository;
import com.paymybuddy.transactionapp.service.Impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UserDetailsServiceTest {

    @Mock
    UserAccountRepository userAccountRepository;

    private UserDetailsService userDetailsService;
    private ConnectedUserDetailsService connectedUserDetailsService;

    @BeforeEach
    public void setup() {
        this.userDetailsService = new UserDetailsServiceImpl(userAccountRepository);
        this.connectedUserDetailsService = new UserDetailsServiceImpl(userAccountRepository);
    }

    @Test
    @DisplayName("loadUserByUsername if UserAccountExists ShouldReturnUserDetails")
    public void testLoadUserByUsername() {
        // GIVEN
        UserAccount userAccount = new UserAccount(1L, "creditor@email.com", "creditor1", "pass123", null, null);

        // WHEN
        when(userAccountRepository.findByEmail(userAccount.getEmail())).thenReturn(Optional.of(userAccount));

        // THEN
        UserDetails userDetails = userDetailsService.loadUserByUsername("creditor@email.com");

        assertThat(userDetails.getUsername()).isEqualTo("creditor@email.com");
        assertThat(userDetails.getPassword()).isEqualTo("pass123");
        assertThat(userDetails.getAuthorities()).hasSize(1);
    }

    @Test
    @DisplayName("When a not found user tried to connection, then throw UserAccountNotFoundException")
    public void testLoadUserByUsernameException() {
        //WHEN
        when(userAccountRepository.findByEmail("notExisting@example.com")).thenReturn(java.util.Optional.empty());
        //THEN
        assertThrows(UserAccountNotFoundException.class , () -> userDetailsService.loadUserByUsername("notExisting@example.com"));
    }


    @Test
    @DisplayName("getConnectedUser returns connected user")
    public void TestGetConnectedUser() {
        //GIVEN
        Authentication authentication = new UsernamePasswordAuthenticationToken("test@example.com", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserAccount userAccount = new UserAccount();
        userAccount.setEmail("test@example.com");

        //WHEN
        when(userAccountRepository.findByEmail("test@example.com")).thenReturn(Optional.of(userAccount));

        UserAccount result = connectedUserDetailsService.getConnectedUser();

        //THEN
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    @DisplayName("When a not found user tried to connection, then throw UserAccountNotFoundException")
    public void TestGetConnectedUserThrowsUserAccountNotFoundException() {
        //GIVEN
        Authentication authentication = new UsernamePasswordAuthenticationToken("test@example.com", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //WHEN
        when(userAccountRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        //THEN
        assertThrows(UserAccountNotFoundException.class , () -> connectedUserDetailsService.getConnectedUser());
    }

    @Test
    @DisplayName("When a not found user tried to connection, then throw AuthenticationCredentialsNotFoundException")
    public void TestGetConnectedUserThrowsAuthenticationCredentialsNotFoundException() {
        // GIVEN
        SecurityContextHolder.getContext().setAuthentication(null);
        //WHEN
        //THEN
        assertThrows(AuthenticationCredentialsNotFoundException.class , () -> connectedUserDetailsService.getConnectedUser());
    }

    @Test
    @DisplayName("When authentication is null, then throw AuthenticationCredentialsNotFoundException")
    public void TestGetConnectedUserWithAuthenticationNullThrowsUserAccountNotFoundException() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //WHEN
        when(userAccountRepository.findByEmail("test@example.com")).thenReturn(null);
        //THEN
        assertThrows(UserAccountNotFoundException.class , () -> connectedUserDetailsService.getConnectedUser());
    }

    @Test
    @DisplayName("When authentication is null, then throw AuthenticationCredentialsNotFoundException")
    public void TestGetConnectedUserWithAuthenticationNullThrowsNullPointerException() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("test@example.com", null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //WHEN
        when(userAccountRepository.findByEmail("test@example.com")).thenReturn(null);
        //THEN
        assertThrows(NullPointerException.class , () -> connectedUserDetailsService.getConnectedUser());
    }

}

