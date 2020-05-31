package com.mans.ecommerce.b2c.domain.exception;

import org.springframework.http.HttpStatus;

public class OutOfStock extends RuntimeDomainException
{
    public OutOfStock(String message)
    {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
