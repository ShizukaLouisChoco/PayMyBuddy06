package com.paymybuddy.transactionapp.exception;

public class FriendAlreadyExistException extends RuntimeException {

    public FriendAlreadyExistException(){super("Friend is already in the friend list of connected user");
    }
    public FriendAlreadyExistException(String msg){super(msg);
    }
}
