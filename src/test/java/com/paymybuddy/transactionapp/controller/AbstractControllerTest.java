package com.paymybuddy.transactionapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.transactionapp.entity.UserAccount;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.web.servlet.ResultActions;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AbstractControllerTest {

    private ObjectMapper objectMapper;

    //to object method
    @SneakyThrows
    protected <T> T toObject(ResultActions resultActions, Class<T> type) {
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(contentAsString,type);
    }

    //for jsonString
    @SneakyThrows
    protected String asJsonString(final Object obj) {
        return objectMapper.writeValueAsString(obj);
    }

    @SneakyThrows
    protected <T> List<T> toList(ResultActions resultActions, Class<T> type) {
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        return objectMapper.readerForListOf(type).readValue(contentAsString);
    }

    @SneakyThrows
    public void createDataStorage ()throws IOException {
        File file = new ClassPathResource("data.json").getFile();
        var data = objectMapper.readValue(file, UserAccount.class);
        //this.dataStorage.setAllData(data);
    }
}
