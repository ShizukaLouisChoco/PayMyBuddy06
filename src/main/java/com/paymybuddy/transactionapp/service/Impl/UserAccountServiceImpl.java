package com.paymybuddy.transactionapp.service.Impl;

import com.paymybuddy.transactionapp.dto.RegisterDto;
import com.paymybuddy.transactionapp.entity.UserAccount;
import com.paymybuddy.transactionapp.exception.EmailAlradyExistException;
import com.paymybuddy.transactionapp.exception.FriendAlreadyExistException;
import com.paymybuddy.transactionapp.exception.UserAccountNotFoundException;
import com.paymybuddy.transactionapp.repository.UserAccountRepository;
import com.paymybuddy.transactionapp.service.UserAccountService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Service
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserAccountServiceImpl(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }


    @Override
    @Transactional
    public UserAccount createUser(RegisterDto user) throws EmailAlradyExistException{
        //verification if the email is already used
        Optional<UserAccount> userExists = userAccountRepository.findByEmail(user.getEmail());

        if(userExists.isPresent()){
            throw new EmailAlradyExistException("Your email address is already registered");
        }
        //if it's not used, save in database with encoded password
        UserAccount newUser = new UserAccount();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPassword( passwordEncoder.encode(user.getPassword()));

        return userAccountRepository.save(newUser);
    }


    @Override
    public UserAccount addFriend(String friendEmail) throws FriendAlreadyExistException {
        UserAccount friend =  getUser(friendEmail);

        UserAccount connectedUser = getConnectedUser();
        //verify if the friend is already in the friend's list of connectedUser
        if(connectedUser.friendExists(friend.getId())) {
            throw new FriendAlreadyExistException("Friend exists on list");
        }
        //if he/she is not in the list, add in the list
        connectedUser.getConnections().add(friend);
        //update connectedUser
        return userAccountRepository.save(connectedUser);
    }


    @Override
    public UserAccount getUser(String email){
        return userAccountRepository.findByEmail(email)
                .orElseThrow(() -> new UserAccountNotFoundException("User not found with email = " + email));
    }


    //function
    @Override
    public void debitBalance(BigDecimal amount) {
        UserAccount connectedUser = getConnectedUser();
        connectedUser.debitAmount(amount);
        userAccountRepository.save(connectedUser);
    }

    @Override
    public void creditBalance(BigDecimal amount) {
        UserAccount connectedUser = getConnectedUser();
        connectedUser.creditAmount(amount);
        // !!!!! Amount + Fare
        userAccountRepository.save(connectedUser);

    }

    public UserAccount getConnectedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getName() != null){
            return getUser(authentication.getName());
        }

        throw new AuthenticationCredentialsNotFoundException("User not connected");
    }

    @Transactional
    public UserAccount update(UserAccount userAccountWithNewInfo) {
        Optional<UserAccount> optionalUserAccount = Optional.of(userAccountRepository.getReferenceById(userAccountWithNewInfo.getId()));
        UserAccount userAccountToUpdate = optionalUserAccount.orElseThrow();
        userAccountToUpdate.setUsername(userAccountWithNewInfo.getUsername());
        userAccountToUpdate.setBalance(userAccountWithNewInfo.getBalance());
        userAccountToUpdate.setConnections(userAccountWithNewInfo.getConnections());
        return userAccountRepository.save(userAccountToUpdate);

    }

    @Override
    public UserAccount getUserById(Long creditorId) {
        return userAccountRepository.getReferenceById(creditorId);
    }
}
