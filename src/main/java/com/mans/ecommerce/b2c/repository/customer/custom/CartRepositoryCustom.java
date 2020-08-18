package com.mans.ecommerce.b2c.repository.customer.custom;

import java.time.Instant;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import org.bson.types.ObjectId;
import reactor.core.publisher.Mono;

public interface CartRepositoryCustom
{
    Mono<Boolean> extendsExpirationDateAndGetActivationStatus(ObjectId id, Instant date);

    Mono<Cart> findAndLock(ObjectId id, Instant date);

}
