package com.mans.ecommerce.b2c.repository.customer.custom;

import java.util.Date;

public interface CartRepositoryCustom
{
    public boolean extendsExpirationDateAndGetActivationStatus(String id, Date date);
}
