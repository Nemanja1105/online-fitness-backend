package org.unibl.etf.exceptions;

public class AlreadyExistsException extends RuntimeException{
    public AlreadyExistsException()
    {
        super("Already exists");
    }

    public AlreadyExistsException(String message)
    {
        super(message);
    }
}
