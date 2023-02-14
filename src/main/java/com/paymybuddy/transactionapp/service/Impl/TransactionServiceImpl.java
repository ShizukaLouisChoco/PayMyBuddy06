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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
     * @return TransactionDto
     */
    @Override
    public TransactionDto createTransaction(TransactionDto transaction) {
        UserAccount connectedUser = userAccountService.getConnectedUser();
        //verifiy balance is ok for transaction
        BigDecimal debitAmount = transaction.getAmountForDebtor();
        if(connectedUser.getBalance().compareTo(debitAmount) <= 0){
            throw new BalanceException();
        }
        Transaction newTransaction = new Transaction(null,userAccountRepository.getReferenceById(transaction.getCreditorId()),connectedUser,transaction.getAmount(),transaction.getDescription(),debitAmount);
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> findByCreditor(Long id) {
        return transactionRepository.findByCreditor(id);
    }


    @Override
    public Page<Transaction> findTransactionPage(UserAccount loggedInUserAccount) {

        Pageable firstPageWithThreeElements = PageRequest.of(0, 3);
        Page<Transaction> allTransaction = (Page<Transaction>) transactionRepository.findAllByDebtor(userAccountService.getConnectedUser().getId(), firstPageWithThreeElements);

        /*
        List<Product> allTenDollarProducts =
                productRepository.findAllByPrice(10, secondPageWithFiveElements);

        Iterable<Transaction> transactionIterable = transactionRepository.findByCreditor(loggedInUserAccount);
        List<Transaction> transactions = new LinkedList<>();
        for(Transaction transaction : transactionIterable){
            transactions.add(transaction);
        }
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<Transaction> list;

        if(transactions.size() < startItem){
            list= Collections.emptyList();
        }else{
            int toIndex = Math.min(startItem + pageSize,transactions.size());
            list = transactions.subList(startItem,toIndex);
        }
        Page<Transaction> transactionPage = new PageImpl<Transaction>(list,PageRequest.of(currentPage, pageSize),transactions.size());*/

        return allTransaction;
    }







    /*public BigDecimal calculateTotalCredit(UUID userAccountId){
        List<Transaction> creditHistory = transactionRepository.findByCreditor(userAccountId);
        creditHistory.stream().map();
        return
    }*/
}
