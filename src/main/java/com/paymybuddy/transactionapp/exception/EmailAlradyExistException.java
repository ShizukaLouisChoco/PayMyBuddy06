package com.paymybuddy.transactionapp.exception;

public class EmailAlradyExistException extends RuntimeException {
    public EmailAlradyExistException() {
        super("Email is already registered in DB");
    }
    public EmailAlradyExistException(String msg) {
        super(msg);
    }
}
