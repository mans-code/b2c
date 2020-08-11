package com.mans.ecommerce.b2c.repository.product.Custom;

import com.mans.ecommerce.b2c.domain.entity.product.subEntity.Reservation;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepositoryCustom
{

    Mono<Integer> lock(String sku, String variationId, ObjectId cartId, int requestedQuantity);

    Mono<Integer> partialLock(String sku, String variationId, ObjectId cartId, int toLock, int newReservedQuantity);

    Mono<Boolean> unlock(String sku, String variationId, ObjectId cartId, int quantity);

    Mono<Boolean> partialUnlock(String sku, String variationId, ObjectId cartId, int toLock, int newReservedQuantity);

    Mono<Boolean> addReservation(Reservation reservation);

    Mono<Boolean> updateReservation(Reservation reservation, int lockedQuantity);

}
