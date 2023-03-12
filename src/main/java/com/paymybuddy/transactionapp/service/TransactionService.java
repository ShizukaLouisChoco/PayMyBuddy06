package com.paymybuddy.transactionapp.service;

import com.paymybuddy.transactionapp.dto.TransactionDto;
import com.paymybuddy.transactionapp.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionService {

    Transaction createTransaction(TransactionDto transaction);

    List<Transaction> findAllByCreditorId(Long id);

    Page<Transaction> findPaginated(Pageable pageable);

}
