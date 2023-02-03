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
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    //create, update
    Transaction save(Transaction transaction);

    //read
    Optional<Transaction>  findById(UUID transactionId);
    List<Transaction> findByCreditor(UUID userAccountId);
    List<Transaction> findByDebtor(UUID userAccountId);

    List<Transaction> findAll();

    //delete
    void deleteById(UUID transactionId);


    TransactionDto save(TransactionDto transaction);



}
