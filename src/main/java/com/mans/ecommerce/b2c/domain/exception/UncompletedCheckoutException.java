package com.mans.ecommerce.b2c.domain.exception;

import java.util.List;

import com.mans.ecommerce.b2c.utill.LockError;
import com.mans.ecommerce.b2c.utill.response.CheckoutResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UncompletedCheckoutException extends RuntimeException
{

    private CheckoutResponse checkoutResponse;

    private List<LockError> uncompleted;

}
