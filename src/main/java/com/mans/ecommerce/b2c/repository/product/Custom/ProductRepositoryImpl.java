package com.mans.ecommerce.b2c.repository.product.Custom;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.google.common.collect.ImmutableMap;
import com.mans.ecommerce.b2c.domain.entity.product.Product;
import com.mans.ecommerce.b2c.domain.entity.product.subEntity.Reservation;
import com.mans.ecommerce.b2c.domain.exception.SystemConstraintViolation;
import com.mans.ecommerce.b2c.server.eventListener.entity.ReservationCreationEvent;
import com.mans.ecommerce.b2c.server.eventListener.entity.ReservationUpdateEvent;
import com.mans.ecommerce.b2c.utill.Global;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.AggregationUpdate;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

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

    private MongoOperations mongoOperations;

    private ApplicationEventPublisher publisher;

    public ProductRepositoryImpl(MongoOperations mongoOperations, ApplicationEventPublisher publisher)
    {
        this.mongoOperations = mongoOperations;
        this.publisher = publisher;
    }

    @Override
    public int lock(String sku, String variationId, String cartId, int requestedQuantity)
    {
        int lockedQuantity = lock(sku, variationId, cartId, requestedQuantity, -1);
        Reservation reservation = new Reservation(sku, variationId, cartId, lockedQuantity);
        publisher.publishEvent(new ReservationCreationEvent(reservation));
        return lockedQuantity;
    }

    @Override public int partialLock(
            String sku,
            String variationId,
            String cartId,
            int requestedQuantity,
            int newReservedQuantity)
    {
        int lockedQuantity = lock(sku, variationId, cartId, requestedQuantity, newReservedQuantity);
        Reservation reservation = new Reservation(sku, variationId, cartId, lockedQuantity);
        publisher.publishEvent(new ReservationUpdateEvent(reservation, lockedQuantity));
        return lockedQuantity;
    }

    @Override
    public boolean unlock(String sku, String variationId, String cartId, int quantity)
    {
        return unlock(sku, variationId, cartId, quantity, -1);
    }

    @Override public boolean partialUnlock(
            String sku,
            String variationId,
            String cartId,
            int quantity,
            int newReservedQuantity)
    {

        return unlock(sku, variationId, cartId, quantity, newReservedQuantity);
    }

    @Override public boolean addReservation(Reservation reservation)
    {

        Query query = getProductQuery(reservation, false);
        Update update = new Update();

        update.push(RESERVATIONS, reservation);

        return executeUpdate(query, update);
    }

    @Override public boolean updateReservation(Reservation reservation, int lockedQuantity)
    {
        Query query = getProductQuery(reservation, true);
        Update update = new Update();

        update.set(RESERVATION_QUANTITY_POSITION, lockedQuantity);

        return executeUpdate(query, update);
    }

    private int lock(String sku, String variationId, String cartId, int requestedQuantity, int newReservedQuantity)
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

        Product product = mongoOperations.findAndModify(query, update, options, Product.class);

        if (Objects.isNull(product))
        {
            String message = "Couldn't lock product, sku=%s\n variationId=%s\n cartId=%s\n withReservation=$s";
            throw new SystemConstraintViolation(String.format(message, sku, variationId, cartId, withReservation));
        }

        int oldQuantity = product
                                  .getAvailability()
                                  .get(variationId)
                                  .getQuantity();

        return getLockQuantity(oldQuantity, requestedQuantity);
    }

    private boolean unlock(String sku, String variationId, String cartId, int quantity, int newReservedQuantity)
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
        String cartId = reservation.getCartId();
        return getProductQuery(sku, variationId, cartId, withReservation);
    }

    private Query getProductQuery(String sku, String variationId, String cartId, boolean withReservation)
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
                                       .otherwise(0));
    }

    private boolean executeUpdate(Query query, Update update)
    {
        UpdateResult result = mongoOperations.updateFirst(query, update, Product.class);

        if ((result.getMatchedCount() != 1 || result.getModifiedCount() != 1))
        {
            return false;
        }

        return true;
    }

    private int getLockQuantity(int productPreQuantity, int requestedQuantity)
    {
        if (productPreQuantity == 0)
        {
            return 0;
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
