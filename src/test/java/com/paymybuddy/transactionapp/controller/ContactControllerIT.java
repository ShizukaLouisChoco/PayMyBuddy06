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
public class ContactControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @WithMockUser(username = "user2@example.com")
    @DisplayName("contact displays contact.html")
    @Test
    public void testContact() throws Exception {
        //GIVEN
        final String url = "/contact";
         // WHEN
        final var response = mockMvc.perform(get(url))
                .andDo(MockMvcResultHandlers.print());

        // THEN
        response.andExpect(status().isOk())
                .andExpect(view().name(url))
                .andExpect(model().attributeExists("userAccount"))
                .andExpect(model().attributeExists("email"))
                .andExpect(model().attributeExists("connections"));
    }
    @WithMockUser(username = "user2@example.com")
    @DisplayName("contact page can add friend by post mapping")
    @Test
    public void testAddContactPostWithFriendEmail() throws Exception {
        //GIVEN
        final String url = "/contact";
        String friendEmail = "user3@example.com";
        // WHEN
        final var response = mockMvc.perform(post(url)
                .with(csrf())
                .param("email",friendEmail));

        // THEN
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(url));
    }
    @WithMockUser(username = "user2@example.com")
    @DisplayName("contact page cannot add friend already exist in friends list by post mapping")
    @Test
    public void testAddContactPostWithFriendEmailAlreadyInList() throws Exception {
        //GIVEN
        final String url = "/contact";
        String friendEmail = "user@example.com";
        // WHEN
        final var response = mockMvc.perform(post(url)
                .with(csrf())
                .param("email",friendEmail));

        // THEN
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name(url))
                .andExpect(model().attributeExists("userAccount"))
                .andExpect(model().attributeExists("email"))
                .andExpect(model().attributeExists("connections"))
                .andExpect(model().attributeExists("errorMsg"));
    }
    @WithMockUser(username = "user2@example.com")
    @DisplayName("contact page can add friend by post mapping")
    @Test
    public void testAddContactPostWithStringNull() throws Exception {
        //GIVEN
        final String url = "/contact";
        String friendEmail = null;
        // WHEN
        final var response = mockMvc.perform(post(url)
                .with(csrf())
                .param("email",friendEmail));

        // THEN
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name(url))
                .andExpect(model().attributeExists("userAccount"))
                .andExpect(model().attributeExists("email"))
                .andExpect(model().attributeExists("connections"))
                .andExpect(model().attributeExists("errorMsg"));
    }
}
