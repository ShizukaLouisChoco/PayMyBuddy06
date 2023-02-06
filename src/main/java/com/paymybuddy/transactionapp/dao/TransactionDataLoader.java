package com.paymybuddy.transactionapp.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.paymybuddy.transactionapp.entity.Transaction;
import com.paymybuddy.transactionapp.repository.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class TransactionDataLoader extends AbstractDataLoader implements CommandLineRunner {

    private final TransactionRepository transactionRepository;

    public TransactionDataLoader(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        TypeReference<List<Transaction>> typeReference = new TypeReference<List<Transaction>>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream("/transaction.json");
        try {
            List<Transaction> transactions = mapper.readValue(inputStream, typeReference);
            transactionRepository.saveAll(transactions);

        } catch (IOException e){
            System.out.println("Unable to save transactions: " + e.getMessage());
        }
    }
}
