package org.unibl.etf.exceptions;

public class NotApprovedException extends RuntimeException{
    public NotApprovedException()
    {
        super("Account don't approved");
    }

    public NotApprovedException(String message)
    {
        super(message);
    }
}
