package com.paymybuddy.transactionapp.controller;

import com.paymybuddy.transactionapp.dto.RegisterDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RegisterControllerTest extends AbstractControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @DisplayName("registerPage displays register.html with getmapping")
    @Test
    public void registerPageGetTest() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("registerDto"));
    }

    @DisplayName("registerPage can create new userAccount with postmapping")
    @Test
    public void registerPagePostTest() throws Exception {
        // GIVEN
        RegisterDto registerDto = new RegisterDto("newuser@example.com","newuser","newuser");

        // WHEN
        final var response  =  mockMvc.perform(post("/register")
                .with(csrf())
                .param("username",registerDto.getUsername())
                .param("email", registerDto.getEmail())
                .param("password", registerDto.getPassword()));
        // THEN
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

    }


    @Test
    public void registerPagePostWithInvalidPAsswordTest() throws Exception {
        // GIVEN
        RegisterDto registerDto = new RegisterDto("user@example.com","aaaaaa","newuser");

        // WHEN
        final var response  =  mockMvc.perform(post("/register")
                .with(csrf())
                .param("username",registerDto.getUsername())
                .param("email", registerDto.getEmail())
                .param("password", registerDto.getPassword()));
        // THEN
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

    }


    @Test
    public void registerPagePostWithInvalidExistingUser() throws Exception {
        // GIVEN
        RegisterDto registerDto = new RegisterDto("newuser@example.com","a","newuser");

        // WHEN
        final var response  =  mockMvc.perform(post("/register")
                .with(csrf())
                .param("username",registerDto.getUsername())
                .param("email", registerDto.getEmail())
                .param("password", registerDto.getPassword()));
        // THEN
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())

                .andExpect(model().attributeExists("userAccount"));

    }


}
