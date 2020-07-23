package com.mans.ecommerce.b2c.domain.exception;

public class ZeroQuantityException extends RuntimeException
{

    public ZeroQuantityException()
    {
        super("you cannot add a product to cart if product quantity is 0");
    }
}
