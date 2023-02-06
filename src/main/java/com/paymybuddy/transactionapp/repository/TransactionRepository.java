package com.paymybuddy.transactionapp.repository;

import com.paymybuddy.transactionapp.dto.TransactionDto;
import com.paymybuddy.transactionapp.entity.Transaction;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@ComponentScan
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    //create, update
    Transaction save(Transaction transaction);

    //read
    Optional<Transaction>  findById(Long transactionId);
    List<Transaction> findByCreditor(Long userAccountId);
    List<Transaction> findByDebtor(Long userAccountId);

    List<Transaction> findAll();

    //delete
    void deleteById(Long transactionId);


    TransactionDto save(TransactionDto transaction);



}
