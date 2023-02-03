package com.paymybuddy.transactionapp.service;

import com.paymybuddy.transactionapp.exception.UserAccountNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailService {
    UserDetails loadUserByUsername(String userEmail) throws UserAccountNotFoundException;
}
