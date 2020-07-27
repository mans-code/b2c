package com.mans.ecommerce.b2c.repository.product.CustomRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mans.ecommerce.b2c.domain.entity.product.Product;
import com.mans.ecommerce.b2c.domain.exception.SystemConstraintViolation;
import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class ProductRepositoryImpl implements ProductRepositoryCustom
{

    private final String QUANTITY_FIELD_TEMPLATE = "availability.%s.quantity";

    private final String PRODUCT_NOT_FOUNT_TEMPLATE = "Couldn't find product with id = %s";

    private final String QUANTITY_NOT_FOUND_TEMPLATE = "Couldn't find quantity for sku= %s variationId = %s";

    private MongoOperations mongoOperations;

    private ObjectMapper objectMapper;

    public ProductRepositoryImpl(MongoOperations mongoOperations, ObjectMapper objectMapper)
    {
        this.mongoOperations = mongoOperations;
        this.objectMapper = objectMapper;
    }

    @Override
    public int lock(String sku, String variationId, int requestedQuantity)
    {
        String quantityField = String.format(QUANTITY_FIELD_TEMPLATE, variationId);
        String ops =
                "db.products.findAndModify("
                        + "{query:{ sku : " + sku + ") },"
                        + "update: [{ $set: { \"" + quantityField + "\": "
                        + "{ $cond: { if: { $gt: [\"$" + quantityField + "\"," + requestedQuantity
                        + "] }, then: {$sum:[ \"$" + quantityField + "\", -" + requestedQuantity
                        + "]}, else:0 } } } } ],"
                        + "new: false,"
                        + "fields: {\"" + quantityField + "\": 1},"
                        + "upsert: false})";

        Integer oldQuantity = mongoOperations.executeCommand(ops).getInteger(quantityField);

        if (oldQuantity == null)
        {
            throw new SystemConstraintViolation(String.format(QUANTITY_NOT_FOUND_TEMPLATE, sku, variationId));
        }

        return getLockQuantity(oldQuantity, requestedQuantity);
    }

    @Override
    public void unlock(String sku, String variationId, int quantity)
    {
        String quantityField = String.format(QUANTITY_FIELD_TEMPLATE, variationId);

        Query query = new Query(Criteria.where("sku").is(sku));

        Update update = new Update();
        update.inc(quantityField, quantity);

        UpdateResult result = mongoOperations.updateFirst(query, update, Product.class);

        if (result.getMatchedCount() == 1 && result.getModifiedCount() == 1)
        {
            throw new SystemConstraintViolation(String.format(QUANTITY_NOT_FOUND_TEMPLATE, sku, variationId));
        }
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
