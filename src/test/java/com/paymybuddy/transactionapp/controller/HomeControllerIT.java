package com.paymybuddy.transactionapp.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @WithMockUser(username = "user@example.com")
    @DisplayName("homePage displays home.html")
    @Test
    public void testHome() throws Exception {
        //GIVEN
        final String url = "/home";

        // WHEN
        final var response = mockMvc.perform(get(url))
                        .andDo(MockMvcResultHandlers.print());


        // THEN
        response.andExpect(status().isOk())
                .andExpect(view().name("/home"))
                .andExpect(model().attributeExists("userAccount"));
    }

}
