package com.mans.ecommerce.b2c.domain.exception;

public class MissingVariationIdException extends RuntimeException
{

    public MissingVariationIdException()
    {
        super("variation id must not be empty");
    }
}
