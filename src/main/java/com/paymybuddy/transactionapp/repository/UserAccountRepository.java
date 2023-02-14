package com.paymybuddy.transactionapp.repository;

import com.paymybuddy.transactionapp.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    //read
    Optional<UserAccount> findByEmail(String userEmail);
}
