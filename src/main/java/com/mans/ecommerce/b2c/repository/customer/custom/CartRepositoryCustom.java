package com.mans.ecommerce.b2c.repository.customer.custom;

import java.util.Date;

import org.bson.types.ObjectId;
import reactor.core.publisher.Mono;

public interface CartRepositoryCustom
{
    public Mono<Boolean> extendsExpirationDateAndGetActivationStatus(ObjectId id, Date date);
}
