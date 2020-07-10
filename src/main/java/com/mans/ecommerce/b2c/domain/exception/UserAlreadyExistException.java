package com.mans.ecommerce.b2c.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserAlreadyExsitException extends RuntimeException
{
    public SignupException()
    {
        super("");
    }
}
