package com.mans.ecommerce.b2c.domain.exception;

public class PaymentFailedException extends RuntimeException
{

    public PaymentFailedException(String message)
    {
        super(message);
    }
}
