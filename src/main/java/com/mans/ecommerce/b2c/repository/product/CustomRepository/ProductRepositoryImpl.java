package com.mans.ecommerce.b2c.repository.product.CustomRepository;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.mans.ecommerce.b2c.domain.entity.product.Product;
import com.mans.ecommerce.b2c.domain.entity.product.subEntity.Reservation;
import com.mans.ecommerce.b2c.domain.exception.SystemConstraintViolation;
import com.mongodb.BasicDBObject;
import com.mongodb.client.result.UpdateResult;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class ProductRepositoryImpl implements ProductRepositoryCustom
{

    private enum ReservationOperation
    {LOCK_UPDATE, UNLOCK_UPDATE, DELETE, ADD}

    private final String RESERVATIONS = "reservations";

    private final String SKU = "sku";

    private final String ID = "id";

    private final String MATCHING = "matching";

    private final String NIN = "$nin";

    private final String ELEMENT_MATCH = "$elemMatch";

    private final String REQ_QTY = "requestedQuantity";

    private final String QUANTITY_FIELD_TEMPLATE = "\"availability.${variationId}.quantity\"";

    private final String RESERVATION_QUANTITY_POSITION = "reservations.$.quantity";

    private final String VARIATION_ID = "variationId";

    private final String QUANTITY_NOT_FOUND_TEMPLATE = "Couldn't find quantity for sku= %s variationId = %s";

    private final String lockQuery =
            "db.products.findAndModify({ "
                    + "query: { sku : \"${sku}\", reservations: { ${matching}: ${l}${reservation}${r} } }, "
                    + "update: ["
                    + "{ $set: { \"availability.${variationId}.quantity\": { $cond: { "
                    + "if: { $gte: [ \"$availability.${variationId}.quantity\", ${requestedQuantity}] }, "
                    + "then: {$sum:[ \"$availability.${variationId}.quantity\", -${requestedQuantity}]}, "
                    + "else: 0 } } } } ], "
                    + "new: false, "
                    + "fields: { \"availability.${variationId}.quantity\": 1 }, "
                    + "upsert: false }) ";

    private final MongoOperations mongoOperations;

    public ProductRepositoryImpl(MongoOperations mongoOperations)
    {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public int lock(String sku, String variationId, String cartId, int requestedQuantity)
    {
        int oldQuantity = executeLock(sku, variationId, cartId, requestedQuantity, -1);
        int lockedQuantity = getLockQuantity(oldQuantity, requestedQuantity);
        addReservation(sku, variationId, cartId, lockedQuantity);
        return lockedQuantity;
    }

    @Override public int partialLock(
            String sku,
            String variationId,
            String cartId,
            int requestedQuantity,
            int newReservedQuantity)
    {
        int oldQuantity = executeLock(sku, variationId, cartId, requestedQuantity, newReservedQuantity);
        int lockedQuantity = getLockQuantity(oldQuantity, requestedQuantity);
        updateReservation(sku, variationId, cartId, lockedQuantity);
        return lockedQuantity;
    }

    @Override
    public void unlock(String sku, String variationId, String cartId, int quantity)
    {
        unlock(sku, variationId, cartId, quantity, -1);
    }

    @Override public void partialUnlock(
            String sku,
            String variationId,
            String cartId,
            int quantity,
            int newReservedQuantity)
    {

        unlock(sku, variationId, cartId, quantity, newReservedQuantity);
    }

    private void addReservation(String sku, String variationId, String cartId, int lockedQuantity)
    {
        Query query = getProductQuery(sku, variationId, cartId, false);
        Reservation reservation = new Reservation(cartId, variationId, lockedQuantity);
        Update update = new Update();

        update.push(RESERVATIONS, reservation);

        executeUpdate(query, update, ReservationOperation.ADD);
    }

    private void updateReservation(String sku, String variationId, String cartId, int lockedQuantity)
    {
        Query query = getProductQuery(sku, variationId, cartId, true);
        Update update = new Update();

        update.set(RESERVATION_QUANTITY_POSITION, lockedQuantity);

        executeUpdate(query, update, ReservationOperation.LOCK_UPDATE);
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

    private void unlock(String sku, String variationId, String cartId, int quantity, int newReservedQuantity)
    {
        Query query = getProductQuery(sku, variationId, cartId, true);
        String quantityField = getString(QUANTITY_FIELD_TEMPLATE,
                                         ImmutableMap.of(VARIATION_ID, variationId));
        Update update = new Update();
        ReservationOperation resOp;

        update.inc(quantityField, quantity);
        if (newReservedQuantity == -1)
        {
            BasicDBObject reservation = getReservation(variationId, cartId);
            update.pull(RESERVATIONS, reservation);
            resOp = ReservationOperation.DELETE;
        }
        else
        {
            update.set(RESERVATION_QUANTITY_POSITION, newReservedQuantity);
            resOp = ReservationOperation.UNLOCK_UPDATE;
        }
        executeUpdate(query, update, resOp);
    }

    private void executeUpdate(
            Query query,
            Update update,
            ReservationOperation resOp)
    {
        UpdateResult result = mongoOperations.updateFirst(query, update, Product.class);

        if ((result.getMatchedCount() != 1 || result.getModifiedCount() != 1))
        {
            String message = resOp + "\n" + query.toString() + "\n" + update.toString();
            throw new SystemConstraintViolation(message);
        }
    }

    private String getString(String template, Map<String, Object> valuesMap)
    {
        StringSubstitutor sub = new StringSubstitutor(valuesMap);
        return sub.replace(template);
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

    private int executeLock(
            String sku,
            String variationId,
            String cartId,
            int requestedQuantity,
            int newReservedQuantity)
    {
        BasicDBObject reservation = getReservation(variationId, cartId);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(SKU, sku);
        map.put(RESERVATIONS, reservation);
        map.put(VARIATION_ID, variationId);
        map.put(REQ_QTY, requestedQuantity);

        if (newReservedQuantity == -1)
        {
            map.put(MATCHING, NIN);
            map.put("l", "[");
            map.put("r", "]");
        }
        else
        {
            map.put(MATCHING, ELEMENT_MATCH);
            map.put("l", "");
            map.put("r", "");
        }

        String query = getString(lockQuery, map);
        String quantityField = String.format(QUANTITY_FIELD_TEMPLATE, variationId);
        Integer oldQuantity = mongoOperations.executeCommand(query).getInteger(quantityField);

        if (oldQuantity == null)
        {
            throw new SystemConstraintViolation(String.format(QUANTITY_NOT_FOUND_TEMPLATE, sku, variationId));
        }

        return oldQuantity;
    }
}
