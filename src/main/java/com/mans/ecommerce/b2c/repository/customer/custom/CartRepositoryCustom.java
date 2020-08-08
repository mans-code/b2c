package com.mans.ecommerce.b2c.repository.customer.custom;

import java.util.Date;

import org.bson.types.ObjectId;

public interface CartRepositoryCustom
{
    public boolean extendsExpirationDateAndGetActivationStatus(ObjectId id, Date date);
}
