package org.unibl.etf.exceptions;

public class AccountBlockedException extends RuntimeException{
    public AccountBlockedException(){
        super("Account is blocked");
    }

    public AccountBlockedException(String message){
        super(message);
    }
}
