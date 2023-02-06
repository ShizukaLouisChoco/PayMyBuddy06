package com.paymybuddy.transactionapp.service;

import com.paymybuddy.transactionapp.dto.TransactionDto;
import com.paymybuddy.transactionapp.entity.Transaction;

import java.util.List;
import java.util.UUID;

public interface TransactionService {

    TransactionDto createTransaction(TransactionDto transaction);

    List<Transaction> findByCreditor(Long id);
    List<Transaction> findByDebtor(Long id);

}
