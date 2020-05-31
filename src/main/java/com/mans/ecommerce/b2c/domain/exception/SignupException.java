package com.mans.ecommerce.b2c.domain.exception;

import org.springframework.http.HttpStatus;

public class SignupException extends RuntimeDomainException
{
    public SignupException()
    {
        super(HttpStatus.BAD_REQUEST, "User already exists");
    }

    public SignupException(String message)
    {
        super(HttpStatus.BAD_REQUEST, message);
    }

}
