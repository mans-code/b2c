package com.mans.ecommerce.b2c.repository.product.Custom;

import com.mans.ecommerce.b2c.domain.entity.product.subEntity.Reservation;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepositoryCustom
{

    int lock(String sku, String variationId, String cartId, int requestedQuantity);

    int partialLock(String sku, String variationId, String cartId, int toLock, int newReservedQuantity);

    boolean unlock(String sku, String variationId, String cartId, int quantity);

    boolean partialUnlock(String sku, String variationId, String cartId, int toLock, int newReservedQuantity);

    boolean addReservation(Reservation reservation);

    boolean updateReservation(Reservation reservation, int lockedQuantity);

}
