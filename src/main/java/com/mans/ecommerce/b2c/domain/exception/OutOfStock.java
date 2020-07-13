package com.mans.ecommerce.b2c.domain.exception;

public class OutOfStock extends RuntimeException
{
    public OutOfStock(String message)
    {

        super(message);
    }
}
