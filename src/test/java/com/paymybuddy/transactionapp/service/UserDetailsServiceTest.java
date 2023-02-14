package com.paymybuddy.transactionapp.service;

import com.paymybuddy.transactionapp.entity.UserAccount;
import com.paymybuddy.transactionapp.exception.UserAccountNotFoundException;
import com.paymybuddy.transactionapp.repository.UserAccountRepository;
import com.paymybuddy.transactionapp.service.Impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UserDetailsServiceTest {

    @Mock
    UserAccountRepository userAccountRepository;

    private UserDetailsService userDetailsService;

    @BeforeEach
    public void setup() {
        this.userDetailsService = new UserDetailsServiceImpl(userAccountRepository);
    }

    @Test
    @DisplayName("loadUserByUsername if UserAccountExists ShouldReturnUserDetails")
    public void testLoadUserByUsername() {
        // GIVEn a user exists
        UserAccount userAccount = new UserAccount(1L, "creditor@email.com", "creditor1", "pass123", null, null);

        // WHEN
        when(userAccountRepository.findByEmail(userAccount.getEmail())).thenReturn(Optional.of(userAccount));

        // THEN
        UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");

        assertThat(userDetails.getUsername()).isEqualTo("test@example.com");
        assertThat(userDetails.getPassword()).isEqualTo("password");
        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(userDetails.getAuthorities().iterator().next().getAuthority()).isEqualTo("USER");
    }

    @Test
    @DisplayName("When a not found user tried to connection, then throw UserAccountNotFoundException")
    public void testLoadUserByUsernameException() {
        when(userAccountRepository.findByEmail("notExisting@example.com")).thenReturn(java.util.Optional.empty());

        assertThrows(UserAccountNotFoundException.class , () -> userDetailsService.loadUserByUsername("notExisting@example.com"));
    }

}

