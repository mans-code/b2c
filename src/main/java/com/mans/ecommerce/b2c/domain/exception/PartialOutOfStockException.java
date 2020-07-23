package com.mans.ecommerce.b2c.domain.exception;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import lombok.Getter;

@Getter
public class PartialOutOfStockException extends RuntimeException
{
    private Cart cart;

    private final String MESSAGE_TEMPLATE = "This product has only %d of these available";

    public PartialOutOfStockException(Cart cart, int lockedQuantity)
    {
        super("This product has only " + lockedQuantity + " of these available");
        this.cart = cart;
    }

}
