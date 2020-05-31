package com.mans.ecommerce.b2c.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RuntimeDomainException extends RuntimeException
{
    private HttpStatus httpStatus;
    private String message;

    public RuntimeDomainException(HttpStatus httpStatus, String message)
    {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
