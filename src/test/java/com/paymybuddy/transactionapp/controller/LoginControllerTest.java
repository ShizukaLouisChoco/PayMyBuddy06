package com.paymybuddy.transactionapp.controller;

import com.paymybuddy.transactionapp.repository.UserAccountRepository;
import com.paymybuddy.transactionapp.service.Impl.UserAccountServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DataJpaTest
public class LoginControllerTest extends AbstractControllerTest{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserAccountRepository userAccountRepository;
    @MockBean
    private UserAccountServiceImpl userAccountService;

    @BeforeEach
    public void init() throws IOException {

    }
    //TODO:this.mockMVC is null?
    @Test
    @DisplayName("/login displays login page")
    public void testLoginPage() throws Exception {
        //WHEN
        final var result = mockMvc.perform(get("/login"));
        //THEN
        result.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("user@example.com can log in with his password")
    public void testLoginSuccess() throws Exception {
        mockMvc.perform(formLogin("/login").user("user@example.com").password("user"))
                .andExpect(status().isFound())
                .andExpect(authenticated().withUsername("user@example.com"));
    }

    @Test
    @DisplayName("user@example cannot log in with wrong password")
    public void testLoginFailure() throws Exception {
        mockMvc.perform(formLogin("/login").user("user@example.com").password("wrongPassword"))
                .andExpect(status().isFound())
                .andExpect(unauthenticated());
    }

    @Test
    @DisplayName("/logout gets connected user deconnected")
    @WithMockUser
    public void testLogout() throws Exception {
        mockMvc.perform(get("/logout"))
                .andExpect(status().isFound())
                .andExpect(unauthenticated());
    }
}
