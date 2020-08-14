package com.mans.ecommerce.b2c.utill.response;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CheckoutResponse
{
    private Cart cart;

    @Value("${app.stripe.public.key}")
    private static String stripePublicKey ;
}
