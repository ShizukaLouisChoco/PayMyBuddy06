package com.paymybuddy.transactionapp.repository;

import com.paymybuddy.transactionapp.dto.TransactionDto;
import com.paymybuddy.transactionapp.entity.Transaction;
import com.paymybuddy.transactionapp.entity.UserAccount;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@ComponentScan
public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long> {

    //create, update
    TransactionDto save(TransactionDto transaction);

    //read
    List<Transaction> findByCreditor(Long userAccountId);

    Transaction save(Transaction transaction);

}
