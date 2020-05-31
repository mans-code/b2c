package com.mans.ecommerce.b2c.domain.exception;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PartialOutOfStock extends RuntimeDomainException
{
    private Cart cart;

    public PartialOutOfStock(String message, Cart cart)
    {
        super(HttpStatus.BAD_REQUEST, message);
        this.cart = cart;
    }

}
