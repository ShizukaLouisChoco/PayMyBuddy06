package com.paymybuddy.transactionapp.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.paymybuddy.transactionapp.entity.Balance;
import com.paymybuddy.transactionapp.repository.BalanceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class BalanceDataLoader extends AbstractDataLoader implements CommandLineRunner  {

    private final BalanceRepository balanceRepository;

    public BalanceDataLoader(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        TypeReference<List<Balance>> typeReference = new TypeReference<List<Balance>>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream("/balance.json");
        try {
            List<Balance> balances = mapper.readValue(inputStream, typeReference);
            balanceRepository.saveAll(balances);

        } catch (IOException e){
            System.out.println("Unable to save balances: " + e.getMessage());
        }
    }
}
