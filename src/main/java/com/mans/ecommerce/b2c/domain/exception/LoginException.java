package com.mans.ecommerce.b2c.domain.exception;

public class LoginException extends RuntimeException
{
    public LoginException(String message)
    {
        super(message);
    }

    public LoginException()
    {
        super("Username or the password is invalid");
    }
}
