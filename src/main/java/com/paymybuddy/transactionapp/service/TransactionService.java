package com.paymybuddy.transactionapp.service;

import com.paymybuddy.transactionapp.dto.TransactionDto;
import com.paymybuddy.transactionapp.entity.Transaction;
import com.paymybuddy.transactionapp.entity.UserAccount;
import com.paymybuddy.transactionapp.exception.TransactionNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionService {

    TransactionDto createTransaction(TransactionDto transaction);

    List<Transaction> findByCreditor(Long id);

    Page<Transaction> findPaginated(Pageable pageable)throws TransactionNotFoundException;

}
