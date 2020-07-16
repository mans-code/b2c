package com.mans.ecommerce.b2c.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExistException extends RuntimeException
{
    public UserAlreadyExistException()
    {
        super("User Already Exists");
    }
}
