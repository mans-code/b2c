package com.mans.ecommerce.b2c.utill.response;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class CheckoutResponse
{
    private String stripePublicKey;

    private Cart cart;
}
