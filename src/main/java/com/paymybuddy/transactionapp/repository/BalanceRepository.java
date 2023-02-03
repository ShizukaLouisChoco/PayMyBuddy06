package com.paymybuddy.transactionapp.repository;

import com.paymybuddy.transactionapp.entity.Balance;
import com.paymybuddy.transactionapp.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, UUID> {

   //create, update
    Balance save(Balance balance);

    //read
    Balance findByUser(UserAccount userAccount);

    List<Balance> findAll();

    //delete

    void deleteByUser(UserAccount userAccount);



}
