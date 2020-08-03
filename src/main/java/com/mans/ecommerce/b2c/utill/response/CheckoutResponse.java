package com.mans.ecommerce.b2c.utill.response;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CheckoutResponse
{
    private Cart cart;

    private String stripePublicKey;
}
