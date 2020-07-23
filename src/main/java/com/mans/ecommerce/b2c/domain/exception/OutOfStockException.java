package com.mans.ecommerce.b2c.domain.exception;

public class OutOfStockException extends RuntimeException
{
    public OutOfStockException()
    {
        super("product is Out Of Stock");
    }
}
