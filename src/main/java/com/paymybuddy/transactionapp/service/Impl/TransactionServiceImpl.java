package com.paymybuddy.transactionapp.service.Impl;

import com.paymybuddy.transactionapp.dto.TransactionDto;
import com.paymybuddy.transactionapp.entity.Transaction;
import com.paymybuddy.transactionapp.repository.TransactionRepository;
import com.paymybuddy.transactionapp.service.TransactionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final static Logger logger = LogManager.getLogger("TransactionServiceImpl");

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * login page needs user's email and password
     * url : http://localhost:8080/login
     * @return UserAccountServiceDto
     */

    /**
     * user make a transaction to friend with
     * url : http://localhost:8080/transaction
     * @return TransactionDto
     */
    @Override
    public TransactionDto createTransaction(TransactionDto transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> findByCreditor(UUID id) {
        return transactionRepository.findByCreditor(id);
    }

    @Override
    public List<Transaction> findByDebtor(UUID id) {
        return transactionRepository.findByDebtor(id);
    }

    /**
     * get person infos with adult and child number by fire station number
     * url : http://localhost:8080/firestation?stationNumber={fireStationNumber}
     * @param fireStationNumber to search
     * @return PersonInfosWithAdultsAndChildrenNumberDTO
     */

    /*public BigDecimal calculateTotalCredit(UUID userAccountId){
        List<Transaction> creditHistory = transactionRepository.findByCreditor(userAccountId);
        creditHistory.stream().map();
        return
    }*/
}
