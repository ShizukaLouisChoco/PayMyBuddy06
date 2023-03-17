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
public class TransferControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @WithMockUser(username = "user@example.com")
    @DisplayName("transfertPage displays transfer.html")
    @Test
    public void testTransferPage() throws Exception {
        //GIVEN
        final String url = "/transfer";

        // WHEN
        final var response = mockMvc.perform(get(url))
                .andDo(MockMvcResultHandlers.print());


        // THEN
        response.andExpect(status().isOk())
                .andExpect(view().name("/transfer"))
                .andExpect(model().attributeExists("userAccount"))
                .andExpect(model().attributeExists("transactionDto"))
                .andExpect(model().attributeExists("connections"))
                .andExpect(model().attributeExists("transactionPage"))
                .andExpect(model().attributeExists("currentPage"));
    }

    @WithMockUser(username = "user2@example.com")
    @DisplayName("transfertPage displays transfer.html")
    @Test
    public void testTransferAddTransaction() throws Exception {
        //GIVEN
        final String url = "/transfer";
        // WHEN
        final var response = mockMvc.perform(post(url)
                .with(csrf())
                .param("creditorId","1")
                .param("description","test")
                .param("amount","10"));


        // THEN
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(url));
    }
    @WithMockUser(username = "user2@example.com")
    @DisplayName("transfertPage with validation error")
    @Test
    public void testTransferPostWithValisationError() throws Exception {
        //GIVEN
        final String url = "/transfer";
        // WHEN
        final var response = mockMvc.perform(post(url)
                .with(csrf())
                .param("creditorId","1")
                .param("description","")
                .param("amount","10"));


        // THEN
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("/transfer"))
                .andExpect(model().attributeExists("transactionDto"))
                .andExpect(model().attributeExists("connections"))
                .andExpect(model().attributeExists("transactionPage"))
                .andExpect(model().attributeExists("currentPage"));
    }

    @WithMockUser(username = "user2@example.com")
    @DisplayName("transfertPage with exception ")
    @Test
    public void testTransferPostWithException() throws Exception {
        //GIVEN
        final String url = "/transfer";
        // WHEN
        final var response = mockMvc.perform(post(url)
                .with(csrf())
                .param("creditorId","3")
                .param("description","test")
                .param("amount","1000"));


        // THEN
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("/transfer"))
                .andExpect(model().attributeExists("transactionDto"))
                .andExpect(model().attributeExists("connections"))
                .andExpect(model().attributeExists("transactionPage"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("errorMsg"));
    }
}
