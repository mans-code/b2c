package com.mans.ecommerce.b2c.domain.exception;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.utill.LockError;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UnableToUpdateCartAfterUncompletedCheckout extends RuntimeException
{
    private Cart cart;

    private List<LockError> lockErrors;
}
