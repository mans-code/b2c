package com.mans.ecommerce.b2c.repository.customer.custom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import com.mans.ecommerce.b2c.domain.entity.customer.Feed;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Mono;
import static org.springframework.data.mongodb.core.query.Criteria.where;

public class FeedRepositoryImpl implements FeedRepositoryCustom
{
    private final String ID = "id";

    private final String SKU = "sku";

    private final String VARIATION_ID = "variationId";

    private final String CART_ADDED = "addedToChart";

    private final String CLICKED = "clicked";

    private ReactiveMongoTemplate mongoTemplate;

    FeedRepositoryImpl(ReactiveMongoTemplate mongoTemplate)
    {
        this.mongoTemplate = mongoTemplate;
    }

    @Override public Mono<Boolean> pushToAddedToCart(
            ObjectId id, ProductInfo productInfo)
    {

        Query query = Query.query(where(ID).is(id));

        BasicDBObject product = getProduct(productInfo);
        Update update = new Update();
        update.push(CART_ADDED, product);

        return mongoTemplate.updateFirst(query, update, Feed.class)
                            .flatMap(res -> Mono.just(res.getModifiedCount() == 1));
    }

    @Override public Mono<Boolean> pushToClicked(ObjectId id, String sku)
    {
        Query query = Query.query(where(ID).is(id));

        Update update = new Update();
        update.push(CLICKED, sku);

        return mongoTemplate.updateFirst(query, update, Feed.class)
                            .flatMap(res -> Mono.just(res.getModifiedCount() == 1));
    }

    @Override public Mono<Boolean> pushToBought(
            ObjectId id, List<ProductInfo> productInfos)
    {
        Query query = Query.query(where(ID).is(id));

        BasicDBObject[] bought = getProductsArray(productInfos);
        Update update = new Update();
        update.push(CLICKED).each(bought);

        return mongoTemplate.updateFirst(query, update, Feed.class)
                            .flatMap(res -> Mono.just(res.getModifiedCount() == 1));
    }

    private BasicDBObject[] getProductsArray(List<ProductInfo> productInfos)
    {
        BasicDBObject[] bought = new BasicDBObject[productInfos.size()];
        IntStream.range(0, bought.length)
                 .forEach(i -> {
                     bought[i] = getProduct(productInfos.get(i));
                 });
        return bought;
    }

    private BasicDBObject getProduct(ProductInfo productInfo)
    {
        Map<String, String> map = new HashMap<>();
        map.put(SKU, productInfo.getSku());
        map.put(VARIATION_ID, productInfo.getVariationId());
        return new BasicDBObject();
    }
}
