package com.paymybuddy.transactionapp.service.Impl;

import com.paymybuddy.transactionapp.dto.TransactionDto;
import com.paymybuddy.transactionapp.entity.Transaction;
import com.paymybuddy.transactionapp.entity.UserAccount;
import com.paymybuddy.transactionapp.exception.BalanceException;
import com.paymybuddy.transactionapp.repository.TransactionRepository;
import com.paymybuddy.transactionapp.repository.UserAccountRepository;
import com.paymybuddy.transactionapp.service.TransactionService;
import com.paymybuddy.transactionapp.service.UserAccountService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    private final UserAccountService userAccountService;
    private final UserAccountRepository userAccountRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, UserAccountService userAccountService, UserAccountRepository userAccountRepository) {
        this.transactionRepository = transactionRepository;
        this.userAccountService = userAccountService;
        this.userAccountRepository = userAccountRepository;
    }

    /**
     * user make a transaction to friend with
     * url : <a href="http://localhost:8080/transaction">...</a>
     *
     * @return TransactionDto
     */
    @Override
    @Transactional
    public TransactionDto createTransaction(TransactionDto transaction) {
        UserAccount connectedUser = userAccountService.getConnectedUser();
        //verifiy balance is ok for transaction
        BigDecimal debitAmount = transaction.getAmountForDebtor();
        if (connectedUser.getBalance().compareTo(debitAmount) <= 0) {
            throw new BalanceException();
        }

        Transaction newTransaction = new Transaction(null, userAccountRepository.getReferenceById(transaction.getCreditorId()), connectedUser, transaction.getAmount(), transaction.getDescription(), debitAmount);
        transactionRepository.save(newTransaction);
        BigDecimal connectedUserBalance = connectedUser.getBalance();
        BigDecimal newConnectedUserBalance = connectedUserBalance.subtract(newTransaction.getDebitAmount());
        connectedUser.setBalance(newConnectedUserBalance);
        userAccountService.update(connectedUser);
        UserAccount creditor = userAccountRepository.getReferenceById(transaction.getCreditorId());
        BigDecimal creditorBalance = creditor.getBalance();
        BigDecimal newCreditorBalance = creditorBalance.add(newTransaction.getAmount());
        creditor.setBalance(newCreditorBalance);
        userAccountService.update(creditor);
        return transaction;
    }

    @Override
    public List<Transaction> findByCreditor(Long id) {
        return transactionRepository.findByCreditor(id);
    }


    @Override
    public Page<Transaction> findPaginated(Pageable pageable) {

        Page<Transaction> page = new PageImpl<>(transactionRepository.findAll(pageable)
                .filter(transaction -> transaction.getDebtor().equals(userAccountService.getConnectedUser())).toList());
        return page;


    }
}

