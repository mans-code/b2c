package com.mans.ecommerce.b2c.repository.product.Custom;

import com.mans.ecommerce.b2c.domain.entity.product.subEntity.Reservation;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepositoryCustom
{

    int lock(String sku, String variationId, ObjectId cartId, int requestedQuantity);

    int partialLock(String sku, String variationId, ObjectId cartId, int toLock, int newReservedQuantity);

    boolean unlock(String sku, String variationId, ObjectId cartId, int quantity);

    boolean partialUnlock(String sku, String variationId, ObjectId cartId, int toLock, int newReservedQuantity);

    boolean addReservation(Reservation reservation);

    boolean updateReservation(Reservation reservation, int lockedQuantity);

}
