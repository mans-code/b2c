package com.mans.ecommerce.b2c.domain.exception;

import java.util.List;

import com.mans.ecommerce.b2c.service.ProductLockInfo;
import com.mans.ecommerce.b2c.utill.response.CheckoutResponse;

public class UncompletedCheckoutException extends RuntimeException
{

    private CheckoutResponse checkoutResponse;

    private List<ProductLockInfo> uncompleted;

    public UncompletedCheckoutException(CheckoutResponse response, List<ProductLockInfo> productLockInfos)
    {
        this.checkoutResponse = response;
        this.uncompleted = productLockInfos;
    }
}
