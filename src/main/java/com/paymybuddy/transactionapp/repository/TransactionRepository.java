package com.paymybuddy.transactionapp.repository;

import com.paymybuddy.transactionapp.entity.Transaction;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@ComponentScan
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    //read
    List<Transaction> findAllByCreditorId(Long userAccountId);
    Page<Transaction> findAllByDebtorId(Long debtorId, Pageable pageable);


}
