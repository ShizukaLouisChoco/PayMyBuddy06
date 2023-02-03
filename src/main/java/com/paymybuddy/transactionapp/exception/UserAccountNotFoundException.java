package com.paymybuddy.transactionapp.exception;

public class UserAccountNotFoundException extends RuntimeException {

    public UserAccountNotFoundException() {
        super("User not found");
    }

    public UserAccountNotFoundException(String msg) {
        super(msg);
    }
}
