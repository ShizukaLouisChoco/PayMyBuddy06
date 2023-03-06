package com.paymybuddy.transactionapp.exception;

public class FriendAddingException extends RuntimeException {

    public FriendAddingException(){super("Friend is already in the friend list of connected user");
    }
    public FriendAddingException(String msg){super(msg);
    }
}
