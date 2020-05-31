package com.mans.ecommerce.b2c.domain.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends RuntimeDomainException
{
    public ResourceNotFoundException(String message)
    {
        super(HttpStatus.NOT_FOUND, message);
    }
}
