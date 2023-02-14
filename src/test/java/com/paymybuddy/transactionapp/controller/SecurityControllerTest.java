package com.paymybuddy.transactionapp.controller;

import com.paymybuddy.transactionapp.dto.LoginDto;
import com.paymybuddy.transactionapp.entity.UserAccount;
import com.paymybuddy.transactionapp.service.Impl.UserAccountServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(LoginController.class)
public class SecurityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserAccountServiceImpl userService;

    @Test
    @DisplayName("/login displays login page")
    public void testLoginPage() throws Exception {

        final var result = mockMvc.perform(get("/login"));
        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("")
    public void testLoginSuccess() throws Exception {
        // setup user
        UserAccount userAccount = new UserAccount(Long.valueOf(001),"test@email.com","username","pass123",null,null);
        when(userService.findByEmail(userAccount.getEmail())).thenReturn(new LoginDto(userAccount));

        // perform login
        mockMvc.perform(formLogin("/login").user(userAccount.getEmail()).password(userAccount.getPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    @DisplayName("")
    public void testLoginFailure() throws Exception {
        // setup user
        UserAccount userAccount = new UserAccount(Long.valueOf(001),"test@email.com","username","pass123",null,null);
        when(userService.findByEmail(userAccount.getEmail())).thenReturn(new LoginDto(userAccount));

        // perform login with incorrect password
    mockMvc.perform(formLogin("/login").user(userAccount.getEmail()).password("wrongpassword"))
                .andExpect(status().isForbidden());
    }
}
