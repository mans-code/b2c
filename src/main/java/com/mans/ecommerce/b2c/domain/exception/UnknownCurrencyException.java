package com.mans.ecommerce.b2c.domain.exception;

import org.springframework.http.HttpStatus;

public class UnknownCurrencyException extends RuntimeException
{
    public UnknownCurrencyException()
    {
        super("Sorry Something went wrong");
    }
}
