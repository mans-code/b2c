package com.mans.ecommerce.b2c.domain.exception;

public class UnauthorizedException extends RuntimeException
{
    public UnauthorizedException()
    {
        super("you must authenticate first");
    }
}
