package com.mans.ecommerce.b2c.domain.exception;

import org.springframework.http.HttpStatus;

public class UnknownCurrency extends RuntimeException
{
    public UnknownCurrency()
    {
        super("Sorry Something went wrong");
    }
}
