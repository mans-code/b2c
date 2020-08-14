package com.mans.ecommerce.b2c.domain.exception;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.utill.LockError;
import com.mans.ecommerce.b2c.utill.response.CheckoutResponse;
import lombok.Getter;

@Getter
public class UncompletedCheckoutException extends RuntimeException
{

    private CheckoutResponse checkoutResponse;

    private String stripePublicKey;

    private List<LockError> uncompleted;

    public UncompletedCheckoutException(Cart cart, List<LockError> productLockInfos)
    {
        this.checkoutResponse = new CheckoutResponse(cart);
        this.uncompleted = productLockInfos;
    }

}
