package com.mans.ecommerce.b2c.domain.exception;

import org.springframework.http.HttpStatus;

public class UnknownCurrency extends RuntimeDomainException
{
    public UnknownCurrency()
    {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Sorry Something went wrong");
    }
}
