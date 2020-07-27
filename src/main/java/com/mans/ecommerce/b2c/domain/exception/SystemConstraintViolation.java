package com.mans.ecommerce.b2c.domain.exception;

public class SystemConstraintViolation extends RuntimeException
{
    public SystemConstraintViolation(String message)
    {
        super(message);
    }
}
