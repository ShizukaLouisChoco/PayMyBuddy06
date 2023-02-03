package com.paymybuddy.transactionapp.repository;

import com.paymybuddy.transactionapp.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, UUID> {

    //create, update
    UserAccount save(UserAccount userAccount);

    //read
    Optional<UserAccount> findOneByEmail(String userEmail);

    //delete
    void deleteById(UUID userAccountId);




}
