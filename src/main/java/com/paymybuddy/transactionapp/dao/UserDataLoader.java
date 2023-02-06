package com.paymybuddy.transactionapp.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.paymybuddy.transactionapp.entity.UserAccount;
import com.paymybuddy.transactionapp.repository.UserAccountRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class UserDataLoader extends AbstractDataLoader implements CommandLineRunner {

    private final UserAccountRepository userAccountRepository;

    public UserDataLoader(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        TypeReference<List<UserAccount>> typeReference = new TypeReference<List<UserAccount>>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream("/user_account.json");
        try {
            List<UserAccount> users = mapper.readValue(inputStream, typeReference);
            userAccountRepository.saveAll(users);

        } catch (IOException e){
            System.out.println("Unable to save users: " + e.getMessage());
        }
    }
}
