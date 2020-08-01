package com.mans.ecommerce.b2c.domain.exception;

public class ConflictException extends RuntimeException
{
    public ConflictException()
    {
        super("cannot perform action at the moment");
    }

    public ConflictException(String message)
    {
        super(message);
    }
}
