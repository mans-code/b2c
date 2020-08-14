package com.mans.ecommerce.b2c.domain.exception;

public class TryAgain extends RuntimeException
{
    public TryAgain()
    {
        super("please try again");
    }
}
