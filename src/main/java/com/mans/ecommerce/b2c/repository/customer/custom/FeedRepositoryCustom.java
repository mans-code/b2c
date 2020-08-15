package com.mans.ecommerce.b2c.repository.customer.custom;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import org.bson.types.ObjectId;
import reactor.core.publisher.Mono;

public interface FeedRepositoryCustom
{
    Mono<Boolean> pushToAddedToCart(ObjectId id, ProductInfo productInfo);

    Mono<Boolean> pushToClicked(ObjectId id, String sku);

    Mono<Boolean> pushToBought(ObjectId id, List<ProductInfo> productInfos);
}
