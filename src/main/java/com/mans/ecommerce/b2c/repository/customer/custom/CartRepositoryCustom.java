package com.mans.ecommerce.b2c.repository.customer.custom;

import java.util.Date;

public interface CartRepositoryCustom
{
    public boolean extendsExpirationDate(String id, Date date);
}
