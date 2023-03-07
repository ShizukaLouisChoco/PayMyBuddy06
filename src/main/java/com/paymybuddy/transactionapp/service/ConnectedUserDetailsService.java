package com.paymybuddy.transactionapp.service;

import com.paymybuddy.transactionapp.entity.UserAccount;

public interface ConnectedUserDetailsService {

    UserAccount getConnectedUser();
}
