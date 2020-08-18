package com.mans.ecommerce.b2c.repository.product.Custom;

import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProductLocking
{

    Mono<Integer> lock(ProductInfo productInfo, ObjectId cartId);

    Mono<Integer> partialLock(ProductInfo productInfo, ObjectId cartId, int toLock);

    void unlock(ProductInfo productInfo, ObjectId cartId);

    void partialUnlock(ProductInfo productInfo, ObjectId cartId, int toLock);
}
