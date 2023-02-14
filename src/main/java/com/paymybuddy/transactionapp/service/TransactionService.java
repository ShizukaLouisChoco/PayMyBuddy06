package com.paymybuddy.transactionapp.service;

import com.paymybuddy.transactionapp.dto.TransactionDto;
import com.paymybuddy.transactionapp.entity.Transaction;
import com.paymybuddy.transactionapp.entity.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TransactionService {

    TransactionDto createTransaction(TransactionDto transaction);

    List<Transaction> findByCreditor(Long id);

    Page<Transaction> findTransactionPage(UserAccount loggedInUserAccount);
}
