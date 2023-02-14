package com.paymybuddy.transactionapp.exception;

public class BalanceException extends RuntimeException {
    public BalanceException(){super("Balance is under the amount + fee of transaction");}
    public BalanceException(String msg){super(msg);}

}
