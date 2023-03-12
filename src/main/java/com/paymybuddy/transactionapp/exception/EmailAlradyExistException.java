package com.paymybuddy.transactionapp.exception;

public class EmailAlradyExistException extends RuntimeException {
    public EmailAlradyExistException(String msg) {
        super(msg);
    }
}
