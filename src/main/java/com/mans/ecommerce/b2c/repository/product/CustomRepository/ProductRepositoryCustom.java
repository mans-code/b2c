package com.mans.ecommerce.b2c.repository.product.CustomRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepositoryCustom
{

    int lock(String sku, String variationId, String cartId, int requestedQuantity);

    int partialLock(String sku, String variationId, String cartId, int toLock, int newReservedQuantity);

    void unlock(String sku, String variationId, String cartId, int quantity);

    void partialUnlock(String sku, String variationId, String cartId, int toLock, int newReservedQuantity);
}
