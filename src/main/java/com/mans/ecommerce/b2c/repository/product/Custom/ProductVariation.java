package com.mans.ecommerce.b2c.repository.product.Custom;

import com.mans.ecommerce.b2c.domain.entity.product.subEntity.Variation;
import reactor.core.publisher.Mono;

public interface ProductVariation
{
    Mono<Variation> getVariation(String sku, String variationId);
}
