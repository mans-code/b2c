package com.mans.ecommerce.b2c.domain.exception;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PartialOutOfStockException extends RuntimeException
{
    private Cart cart;

    private String message;

    public PartialOutOfStockException(Cart cart, int lockedQuantity)
    {
        this.message = "This product has only " + lockedQuantity + " of these available";
        this.cart = cart;
    }

}
