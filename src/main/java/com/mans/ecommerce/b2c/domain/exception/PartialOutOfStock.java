package com.mans.ecommerce.b2c.domain.exception;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PartialOutOfStock extends RuntimeException
{
    private Cart cart;

    public PartialOutOfStock(String message, Cart cart)
    {
        super(message);
        this.cart = cart;
    }

}
