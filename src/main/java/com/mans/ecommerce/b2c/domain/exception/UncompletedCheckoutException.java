package com.mans.ecommerce.b2c.domain.exception;

import java.util.List;

import com.mans.ecommerce.b2c.utill.ProductLockErrorInfo;
import com.mans.ecommerce.b2c.utill.response.CheckoutResponse;
import lombok.Getter;

@Getter
public class UncompletedCheckoutException extends RuntimeException
{

    private CheckoutResponse checkoutResponse;

    private List<ProductLockErrorInfo> uncompleted;

    public UncompletedCheckoutException(CheckoutResponse response, List<ProductLockErrorInfo> productLockInfos)
    {
        this.checkoutResponse = response;
        this.uncompleted = productLockInfos;
    }
}
