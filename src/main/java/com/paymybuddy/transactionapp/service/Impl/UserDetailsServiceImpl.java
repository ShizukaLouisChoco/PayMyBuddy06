package com.paymybuddy.transactionapp.service.Impl;

import com.paymybuddy.transactionapp.entity.UserAccount;
import com.paymybuddy.transactionapp.exception.UserAccountNotFoundException;
import com.paymybuddy.transactionapp.repository.UserAccountRepository;
import com.paymybuddy.transactionapp.service.ConnectedUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService , ConnectedUserDetailsService {

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
                .orElseThrow(UserAccountNotFoundException::new);
    }


    public UserAccount getConnectedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getName() != null){
            return userAccountRepository.findByEmail(authentication.getName())
                    .orElseThrow(UserAccountNotFoundException::new);
        }

        throw new AuthenticationCredentialsNotFoundException("User not connected");
    }
}
