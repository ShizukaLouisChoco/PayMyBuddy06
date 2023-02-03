package com.paymybuddy.transactionapp.service.Impl;

import com.paymybuddy.transactionapp.exception.UserAccountNotFoundException;
import com.paymybuddy.transactionapp.repository.UserAccountRepository;
import com.paymybuddy.transactionapp.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UserAccountNotFoundException {
        return userAccountRepository.findOneByEmail(userEmail)
                .map(user -> new User(
                        user.getEmail(),
                        user.getPassword(),
                        null
                ))
                .orElseThrow(UserAccountNotFoundException::new);
    }
}
