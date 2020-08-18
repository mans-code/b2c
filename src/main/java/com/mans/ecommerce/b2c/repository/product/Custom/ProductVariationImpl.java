package com.mans.ecommerce.b2c.repository.product.Custom;

import com.mans.ecommerce.b2c.domain.entity.product.Product;
import com.mans.ecommerce.b2c.domain.entity.product.subEntity.Variation;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;
import static org.springframework.data.mongodb.core.query.Criteria.where;

public class ProductVariationImpl implements ProductVariation
{

    private final String SKU = "sku";

    private final String VARIATIONS_DETAILS_TEMPLATE = "variationsDetails.%s";

    private ReactiveMongoTemplate mongoTemplate;

    ProductVariationImpl(ReactiveMongoTemplate mongoTemplate)
    {
        this.mongoTemplate = mongoTemplate;
    }

    @Override public Mono<Variation> getVariation(String sku, String variationId)
    {
        String wantedVariation = String.format(VARIATIONS_DETAILS_TEMPLATE, variationId);

        Query query = new Query();
        query.addCriteria(where(SKU).is(sku));
        query.fields().include(wantedVariation);

        Mono<Product> productMono = mongoTemplate.findOne(query, Product.class);

        return productMono.flatMap(product -> {
            Variation variation = product.getVariationsDetails()
                                         .get(variationId);
            return Mono.just(variation);
        });
    }

}
