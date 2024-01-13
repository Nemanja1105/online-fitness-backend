package org.unibl.etf.exceptions;

public class PasswordMismatchException extends RuntimeException{
    public PasswordMismatchException(){
        super("Password do not match!!");
    }

    public PasswordMismatchException(String message){
        super(message);
    }
}
