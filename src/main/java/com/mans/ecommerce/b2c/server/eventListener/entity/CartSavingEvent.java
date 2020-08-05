package com.mans.ecommerce.b2c.server.eventListener.entity;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CartSavingEvent
{
    private Cart cart;
}
