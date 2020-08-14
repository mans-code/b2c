package com.mans.ecommerce.b2c.repository.customer.custom;

import java.util.Date;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import org.bson.types.ObjectId;
import reactor.core.publisher.Mono;

public interface CartRepositoryCustom
{
    Mono<Boolean> extendsExpirationDateAndGetActivationStatus(ObjectId id, Date date);

    Mono<Cart> findAndLock(ObjectId id, Date date);

}
