package com.paymybuddy.transactionapp.service.Impl;

import com.paymybuddy.transactionapp.entity.UserAccount;
import com.paymybuddy.transactionapp.exception.UserAccountNotFoundException;
import com.paymybuddy.transactionapp.repository.UserAccountRepository;
import com.paymybuddy.transactionapp.service.UserDetailService;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailService {

    private final UserAccountRepository userAccountRepository;

    public UserDetailsServiceImpl(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UserAccountNotFoundException {

        return userAccountRepository.findByEmail(email)
                .map(user -> new User(
                        user.getEmail(),
                        user.getPassword(),
                        AuthorityUtils.createAuthorityList("USER")
                ))
                .orElseThrow(UserAccountNotFoundException::new);}
}
