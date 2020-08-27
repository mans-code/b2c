package com.mans.ecommerce.b2c.repository.product.Custom;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.mans.ecommerce.b2c.domain.entity.product.Product;
import com.mans.ecommerce.b2c.domain.entity.product.subEntity.Reservation;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import com.mans.ecommerce.b2c.utill.Global;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationUpdate;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ProductLockingImpl implements ProductLocking
{

    private final String RESERVATIONS = "reservations";

    private final String SKU = "sku";

    private final String ID = "id";

    private final String SUM = "$sum";

    private final String REF = "$";

    private final String QUANTITY_FIELD_TEMPLATE = "variationsDetails.${variationId}.availability.quantity";

    private final String RESERVATION_QUANTITY_POSITION = "reservations.$.quantity";

    private final String VARIATION_ID = "variationId";

    private final int ZERO = 0;

    private ReactiveMongoTemplate mongoTemplate;

    private ApplicationEventPublisher publisher;

    public ProductLockingImpl(ReactiveMongoTemplate mongolTemplate, ApplicationEventPublisher publisher)
    {
        this.mongoTemplate = mongolTemplate;
        this.publisher = publisher;
    }

    @Override
    public Mono<Integer> lock(ProductInfo productInfo, ObjectId cartId)
    {
        String resId = cartId.toHexString();
        return lock(productInfo, resId, productInfo.getQuantity(), false)
                       .doOnSuccess(qty -> addReservation(productInfo, resId, qty));
    }

    @Override public Mono<Integer> partialLock(
            ProductInfo productInfo,
            ObjectId cartId,
            int requestedQuantity)
    {
        String resId = cartId.toHexString();
        return lock(productInfo, resId, requestedQuantity, true)
                       .doOnSuccess(qty -> updateReservation(productInfo, resId, qty));

    }

    @Override
    public void unlock(ProductInfo productInfo, ObjectId cartId)
    {
        String resId = cartId.toHexString();
        unlock(productInfo, resId, productInfo.getQuantity(), false).subscribe();
    }

    @Override public void partialUnlock(
            ProductInfo productInfo,
            ObjectId cartId,
            int quantity)
    {
        String resId = cartId.toHexString();
        unlock(productInfo, resId, quantity, true).subscribe();
    }

    private void addReservation(ProductInfo productInfo, String cartId, Integer lockedQuantity)
    {
        reservation(productInfo, cartId, lockedQuantity, true);
    }

    private void updateReservation(ProductInfo productInfo, String cartId, Integer lockedQuantity)
    {
        reservation(productInfo, cartId, lockedQuantity, false);
    }

    void reservation(ProductInfo productInfo, String cartId, Integer locked, boolean add)
    {

        if (locked == 0)
        {
            return;
        }

        Reservation reservation = new Reservation(productInfo, cartId, locked);
        Query query = getProductQuery(reservation, !add);
        Update update = new Update();

        if (add)
        {
            update.push(RESERVATIONS, reservation);
        }
        else
        {
            update.inc(RESERVATION_QUANTITY_POSITION, locked);
        }

        executeUpdate(query, update).subscribe(); //TODO handle error
    }

    private Mono<Integer> lock(
            ProductInfo productInfo,
            String cartId,
            int requestedQuantity,
            boolean withReservation)
    {
        String sku = productInfo.getSku();
        String variationId = productInfo.getVariationId();
        String quantityField = Global.getString(QUANTITY_FIELD_TEMPLATE,
                                                ImmutableMap.of(VARIATION_ID, variationId));

        Query query = getProductQuery(sku, variationId, cartId, withReservation);
        query.fields().include(quantityField);

        AggregationUpdate update = getLockUpdate(quantityField, requestedQuantity);

        FindAndModifyOptions options = FindAndModifyOptions
                                               .options()
                                               .returnNew(false)
                                               .upsert(false)
                                               .remove(false);

        Mono<Product> productMono = mongoTemplate.findAndModify(query, update, options, Product.class);

        return getLockQuantity(productMono, variationId, requestedQuantity);
    }

    private Mono<Integer> getLockQuantity(
            Mono<Product> productMono,
            String variationId,
            int requestedQuantity)
    {
        return productMono.flatMap(product -> {
            int oldQuantity = product
                                      .getVariationsDetails()
                                      .get(variationId)
                                      .getAvailability()
                                      .getQuantity();

            int lockedQuantity = getLockQuantity(oldQuantity, requestedQuantity);

            return Mono.just(lockedQuantity);
        }).defaultIfEmpty(ZERO);
    }

    private Mono<Boolean> unlock(ProductInfo productInfo, String cartId, int quantity, boolean updateReservation)
    {
        String sku = productInfo.getSku();
        String variationId = productInfo.getVariationId();

        Query query = getProductQuery(sku, variationId, cartId, true);
        String quantityField = Global.getString(QUANTITY_FIELD_TEMPLATE,
                                                ImmutableMap.of(VARIATION_ID, variationId));

        Update update = new Update();
        update.inc(quantityField, quantity);

        if (updateReservation)
        {
            update.inc(RESERVATION_QUANTITY_POSITION, -quantity);

        }
        else
        {
            BasicDBObject reservation = getReservation(variationId, cartId);
            update.pull(RESERVATIONS, reservation);
        }
        return executeUpdate(query, update);
    }

    private Query getProductQuery(Reservation reservation, boolean withReservation)
    {
        String sku = reservation.getSku();
        String variationId = reservation.getVariationId();
        String resId = reservation.getId();
        return getProductQuery(sku, variationId, resId, withReservation);
    }

    private Query getProductQuery(String sku, String variationId, String cartId, boolean withReservation)
    {
        Query query = new Query();
        query.addCriteria(Criteria.where(SKU).is(sku));

        if (withReservation)
        {
            query.addCriteria(Criteria.where(RESERVATIONS)
                                      .elemMatch(Criteria.where(ID).is(cartId)
                                                         .and(VARIATION_ID).is(variationId)));
        }
        else
        {
            BasicDBObject reservation = getReservation(variationId, cartId);
            query.addCriteria(Criteria.where(RESERVATIONS).nin(reservation));
        }

        return query;
    }

    private BasicDBObject getReservation(String variationId, String cartId)
    {
        Map map = new HashMap();
        map.put(ID, cartId);
        map.put(VARIATION_ID, variationId);
        return new BasicDBObject(map);
    }

    private AggregationUpdate getLockUpdate(String quantityField, int requestedQty)
    {
        return AggregationUpdate
                       .update()
                       .set(quantityField)
                       .toValue(
                               ConditionalOperators
                                       .Cond
                                       .when(Criteria.where(quantityField).gte(requestedQty))
                                       .thenValueOf(context -> {
                                           Document sumExpression = new Document();
                                           BasicDBList list = new BasicDBList();
                                           list.add(REF + quantityField);
                                           list.add(requestedQty * -1);
                                           sumExpression.put(SUM, list);
                                           return new Document().append(SUM, list);
                                       })
                                       .otherwise(ZERO));
    }

    private Mono<Boolean> executeUpdate(Query query, Update update)
    {
        return mongoTemplate
                       .updateFirst(query, update, Product.class)
                       .flatMap(result ->
                                {
                                    Boolean found = result.getModifiedCount() > ZERO;
                                    return Mono.just(found);
                                });
    }

    private int getLockQuantity(int productPreQuantity, int requestedQuantity)
    {
        if (productPreQuantity == ZERO)
        {
            return ZERO;
        }
        else
        {
            return Math.min(productPreQuantity, requestedQuantity);
        }
    }
}
