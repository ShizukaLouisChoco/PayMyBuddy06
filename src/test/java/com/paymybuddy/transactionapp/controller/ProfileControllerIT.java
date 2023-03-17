package com.paymybuddy.transactionapp.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProfileControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @WithMockUser(username = "user@example.com")
    @DisplayName("profile displays profile.html")
    @Test
    public void testProfile() throws Exception {
        //GIVEN
        final String url = "/profile";

        // WHEN
        final var response = mockMvc.perform(get(url))
                .andDo(MockMvcResultHandlers.print());


        // THEN
        response.andExpect(status().isOk())
                .andExpect(view().name("/profile"))
                .andExpect(model().attributeExists("userAccount"))
                .andExpect(model().attributeExists("transferDto"))
                .andExpect(model().attributeExists("creditAmount"))
                .andExpect(model().attributeExists("creditAmountDto"));
    }

    @WithMockUser(username = "user2@example.com")
    @DisplayName("profile/credit can credit money to balance")
    @Test
    public void testCreditToBank() throws Exception {
        //GIVEN
        final String url = "/profile/credit";
        // WHEN
        final var response = mockMvc.perform(post(url)
                .with(csrf())
                .param("creditAmount","100"));

        // THEN
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"));
    }

    @WithMockUser(username = "user2@example.com")
    @DisplayName("/profile/credit with validation error")
    @Test
    public void testCreditToBankWithValidationError() throws Exception {
        //GIVEN
        final String url = "/profile/credit";
        // WHEN
        final var response = mockMvc.perform(post(url)
                .with(csrf())
                .param("creditAmount","-10"));


        // THEN
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("/profile"))
                .andExpect(model().attributeExists("userAccount"))
                .andExpect(model().attributeExists("transferDto"))
                .andExpect(model().attributeExists("creditAmount"))
                .andExpect(model().attributeExists("creditAmountDto"));
    }
    //TODO
    /*
    @WithMockUser(username = "user2@example.com")
    @DisplayName("profile/credit with exception ")
    @Test
    public void testCreditToBankWithException() throws Exception {
        //GIVEN
        final String url = "/profile/credit";
        // WHEN
        final var response = mockMvc.perform(post(url)
                .with(csrf())
                .param("creditAmount",""));


        // THEN
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("/profile"))
                .andExpect(model().attributeExists("userAccount"))
                .andExpect(model().attributeExists("transferDto"))
                .andExpect(model().attributeExists("creditAmount"))
                .andExpect(model().attributeExists("creditAmountDto"))
                .andExpect(model().attributeExists("errorMsg"));
    }*/

    @WithMockUser(username = "user2@example.com")
    @DisplayName("profile/debit can credit money to balance")
    @Test
    public void testDebitToBank() throws Exception {
        //GIVEN
        final String url = "/profile/debit";
        // WHEN
        final var response = mockMvc.perform(post(url)
                .with(csrf())
                .param("titulaire","abc")
                .param("rib","12345678901234567890123")
                .param("iban","123456789012345678901234567")
                .param("swift","12345678")
                .param("debitAmount","10"));

        // THEN
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"));
    }

    @WithMockUser(username = "user2@example.com")
    @DisplayName("/profile/debit with validation error")
    @Test
    public void testDebitToBankWithValidationError() throws Exception {
        //GIVEN
        final String url = "/profile/debit";
        // WHEN
        final var response = mockMvc.perform(post(url)
                .with(csrf())
                .param("titulaire","abc")
                .param("rib","1")
                .param("iban","123456789012345678901234567")
                .param("swift","12345678")
                .param("debitAmount","10"));


        // THEN
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("/profile"))
                .andExpect(model().attributeExists("userAccount"))
                .andExpect(model().attributeExists("transferDto"))
                .andExpect(model().attributeExists("creditAmount"))
                .andExpect(model().attributeExists("creditAmountDto"));
    }
    @WithMockUser(username = "user2@example.com")
    @DisplayName("profile/debit with exception ")
    @Test
    public void testDebitToBankWithException() throws Exception {
        //GIVEN
        final String url = "/profile/debit";
        // WHEN
        final var response = mockMvc.perform(post(url)
                .with(csrf())
                .param("titulaire","abc")
                .param("rib","12345678901234567890123")
                .param("iban","123456789012345678901234567")
                .param("swift","12345678")
                .param("debitAmount","1000"));


        // THEN
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("/profile"))
                .andExpect(model().attributeExists("userAccount"))
                .andExpect(model().attributeExists("transferDto"))
                .andExpect(model().attributeExists("creditAmount"))
                .andExpect(model().attributeExists("creditAmountDto"))
                .andExpect(model().attributeExists("errorMsg"));
    }
}
