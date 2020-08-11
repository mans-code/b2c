package com.mans.ecommerce.b2c.repository.product.Custom;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.mans.ecommerce.b2c.domain.entity.product.Product;
import com.mans.ecommerce.b2c.domain.entity.product.subEntity.Reservation;
import com.mans.ecommerce.b2c.server.eventListener.entity.ReservationCreationEvent;
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
public class ProductRepositoryImpl implements ProductRepositoryCustom
{

    private final String RESERVATIONS = "reservations";

    private final String SKU = "sku";

    private final String ID = "id";

    private final String SUM = "$sum";

    private final String REF = "$";

    private final String QUANTITY_FIELD_TEMPLATE = "\"availability.${variationId}.quantity\"";

    private final String RESERVATION_QUANTITY_POSITION = "reservations.$.quantity";

    private final String VARIATION_ID = "variationId";

    private final int ZERO = 0;

    private ReactiveMongoTemplate mongoTemplate;

    private ApplicationEventPublisher publisher;

    public ProductRepositoryImpl(ReactiveMongoTemplate mongoTemplate, ApplicationEventPublisher publisher)
    {
        this.mongoTemplate = mongoTemplate;
        this.publisher = publisher;
    }

    @Override
    public Mono<Integer> lock(String sku, String variationId, ObjectId cartId, int requestedQuantity)
    {
        Mono<Integer> lockedQuantity = lock(sku, variationId, cartId, requestedQuantity, -1);
        publishReservationEvent(sku, variationId, cartId, lockedQuantity);
        return lockedQuantity;
    }

    @Override public Mono<Integer> partialLock(
            String sku,
            String variationId,
            ObjectId cartId,
            int requestedQuantity,
            int newReservedQuantity)
    {
        Mono<Integer> lockedQuantity = lock(sku, variationId, cartId, requestedQuantity, newReservedQuantity);
        publishReservationEvent(sku, variationId, cartId, lockedQuantity);
        return lockedQuantity;
    }

    @Override
    public Mono<Boolean> unlock(String sku, String variationId, ObjectId cartId, int quantity)
    {
        return unlock(sku, variationId, cartId, quantity, -1);
    }

    @Override public Mono<Boolean> partialUnlock(
            String sku,
            String variationId,
            ObjectId cartId,
            int quantity,
            int newReservedQuantity)
    {

        return unlock(sku, variationId, cartId, quantity, newReservedQuantity);
    }

    @Override public Mono<Boolean> addReservation(Reservation reservation)
    {

        Query query = getProductQuery(reservation, false);
        Update update = new Update();

        update.push(RESERVATIONS, reservation);

        return executeUpdate(query, update);
    }

    @Override public Mono<Boolean> updateReservation(Reservation reservation, int lockedQuantity)
    {
        Query query = getProductQuery(reservation, true);
        Update update = new Update();

        update.set(RESERVATION_QUANTITY_POSITION, lockedQuantity);

        return executeUpdate(query, update);
    }

    private Mono<Integer> lock(
            String sku,
            String variationId,
            ObjectId cartId,
            int requestedQuantity,
            int newReservedQuantity)
    {
        boolean withReservation = newReservedQuantity == -1;
        String quantityField = Global.getString(QUANTITY_FIELD_TEMPLATE,
                                                ImmutableMap.of(VARIATION_ID, variationId));

        Query query = getProductQuery(sku, variationId, cartId, withReservation);
        query.fields().include(quantityField).exclude(ID);

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
    {//TODO test If empty
        return productMono.flatMap(product -> {
            int oldQuantity = product
                                      .getAvailability()
                                      .get(variationId)
                                      .getQuantity();

            int lockedQuantity = getLockQuantity(oldQuantity, requestedQuantity);

            return Mono.just(lockedQuantity);
        }).defaultIfEmpty(ZERO);
    }

    private void publishReservationEvent(
            String sku,
            String variationId,
            ObjectId cartId,
            Mono<Integer> lockedQuantity)
    {

        lockedQuantity.doOnSuccess(qty -> {
            if (qty > ZERO)
            {
                Reservation reservation = new Reservation(sku, variationId, cartId, qty);
                publisher.publishEvent(new ReservationCreationEvent(reservation));
            }
        });
    }

    private Mono<Boolean> unlock(String sku, String variationId, ObjectId cartId, int quantity, int newReservedQuantity)
    {
        Query query = getProductQuery(sku, variationId, cartId, true);
        String quantityField = Global.getString(QUANTITY_FIELD_TEMPLATE,
                                                ImmutableMap.of(VARIATION_ID, variationId));

        Update update = new Update();
        update.inc(quantityField, quantity);

        if (newReservedQuantity == -1)
        {
            BasicDBObject reservation = getReservation(variationId, cartId);
            update.pull(RESERVATIONS, reservation);
        }
        else
        {
            update.set(RESERVATION_QUANTITY_POSITION, newReservedQuantity);
        }
        return executeUpdate(query, update);
    }

    private Query getProductQuery(Reservation reservation, boolean withReservation)
    {
        String sku = reservation.getSku();
        String variationId = reservation.getVariationId();
        ObjectId cartId = reservation.getCartId();
        return getProductQuery(sku, variationId, cartId, withReservation);
    }

    private Query getProductQuery(String sku, String variationId, ObjectId cartId, boolean withReservation)
    {
        BasicDBObject reservation = getReservation(variationId, cartId);
        Query query = new Query();

        query.addCriteria(Criteria.where(SKU).is(sku));

        if (withReservation)
        {
            query.addCriteria(Criteria.where(RESERVATIONS)
                                      .elemMatch(Criteria.where(ID).is(cartId).and(VARIATION_ID).is(variationId)));
        }
        else
        {
            query.addCriteria(Criteria.where(RESERVATIONS).nin(reservation));
        }

        return query;
    }

    private BasicDBObject getReservation(String variationId, ObjectId cartId)
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
        else if (productPreQuantity < requestedQuantity)
        {
            return productPreQuantity;
        }
        else
        {
            return requestedQuantity;
        }
    }
}
