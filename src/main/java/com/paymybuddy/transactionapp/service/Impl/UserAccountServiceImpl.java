package com.paymybuddy.transactionapp.service.Impl;

import com.paymybuddy.transactionapp.dto.RegisterDto;
import com.paymybuddy.transactionapp.entity.UserAccount;
import com.paymybuddy.transactionapp.exception.BalanceException;
import com.paymybuddy.transactionapp.exception.EmailAlreadyExistException;
import com.paymybuddy.transactionapp.exception.FriendAddingException;
import com.paymybuddy.transactionapp.exception.UserAccountNotFoundException;
import com.paymybuddy.transactionapp.repository.UserAccountRepository;
import com.paymybuddy.transactionapp.service.ConnectedUserDetailsService;
import com.paymybuddy.transactionapp.service.UserAccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

    private final ConnectedUserDetailsService connectedUserDetailsService;
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;



    @Override
    @Transactional
    public UserAccount createUser(RegisterDto user) throws EmailAlreadyExistException {
        //verification if the email is already used
        Optional<UserAccount> userExists = userAccountRepository.findByEmail(user.getEmail());

        if(userExists.isPresent()){
            throw new EmailAlreadyExistException("Your email address is already registered");
        }
        //if it's not used, save in database with encoded password
        UserAccount newUser = new UserAccount();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPassword( passwordEncoder.encode(user.getPassword()));

        return userAccountRepository.save(newUser);
    }


    @Override
    @Transactional
    public UserAccount addFriend(String friendEmail) throws FriendAddingException {
        UserAccount friend =  getUser(friendEmail);

        UserAccount connectedUser = connectedUserDetailsService.getConnectedUser();
        //verify if the friend is already in the friend's list of connectedUser
        if(connectedUser.friendExists(friend.getId())) {
            throw new FriendAddingException("This friend is already in your contact list");
        }
        if(friend.equals(connectedUser)){
            throw new FriendAddingException("You cannot add yourself in contact list");
        }
        //if he/she is not in the list, add in the list
        connectedUser.getFriends().add(friend);
        //update connectedUser
        return userAccountRepository.save(connectedUser);
    }

    @Override
    public UserAccount getUser(String email){
        return userAccountRepository.findByEmail(email)
                .orElseThrow(() -> new UserAccountNotFoundException("User not found with email = " + email ));
    }


    //function
    @Override
    @Transactional
    public void debitBalance(BigDecimal amount) {
        UserAccount connectedUser = connectedUserDetailsService.getConnectedUser();
        BigDecimal actualBalance = connectedUser.getBalance();
        if(actualBalance.compareTo(amount) < 0){
            throw new BalanceException("Your balance is not enough to transfer this amount");
        }
        connectedUser.debitAmount(amount);
        userAccountRepository.save(connectedUser);
    }

    @Override
    @Transactional
    public void creditBalance(BigDecimal creditAmount) {
        UserAccount connectedUser = connectedUserDetailsService.getConnectedUser();
        connectedUser.creditAmount(creditAmount);
        userAccountRepository.save(connectedUser);

    }


    @Transactional
    public UserAccount update(UserAccount userAccountWithNewInfo) {
        Optional<UserAccount> optionalUserAccount = Optional.of(userAccountRepository.getReferenceById(userAccountWithNewInfo.getId()));
        UserAccount userAccountToUpdate = optionalUserAccount.orElseThrow();
        userAccountToUpdate.setUsername(userAccountWithNewInfo.getUsername());
        userAccountToUpdate.setBalance(userAccountWithNewInfo.getBalance());
        userAccountToUpdate.setFriends(userAccountWithNewInfo.getFriends());
        return userAccountRepository.save(userAccountToUpdate);

    }

    @Override
    public UserAccount getUserById(Long creditorId) {
        return userAccountRepository.getReferenceById(creditorId);
    }
}
