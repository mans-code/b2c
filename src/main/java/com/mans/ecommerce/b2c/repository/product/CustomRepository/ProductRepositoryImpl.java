package com.mans.ecommerce.b2c.repository.product.CustomRepository;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mans.ecommerce.b2c.domain.entity.product.Product;
import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class ProductRepositoryImpl implements ProductRepositoryCustom
{

    private final String QUANTITY_FIELD = "availability.numUnitsAvailable";

    private final String PRODUCT_NOT_FOUNT_TEMPLATE = "Couldn't find product with id = %s";

    private MongoOperations mongoOperations;

    private ObjectMapper objectMapper;

    public ProductRepositoryImpl(MongoOperations mongoOperations, ObjectMapper objectMapper)
    {
        this.mongoOperations = mongoOperations;
        this.objectMapper = objectMapper;
    }

    @Override
    public int lock(String sku, String variationId, int quantity)
    {
        String quantityField = String.format("availability.%s.quantity", variationId);
        String ops =
                "db.products.findAndModify("
                        + "{query:{ sku : " + sku + ") },"
                        + "update: [{ $set: { \"" + quantityField + "\": "
                        + "{ $cond: { if: { $gt: [\"$" + quantityField + "\"," + quantity
                        + "] }, then: {$sum:[ \"$" + quantityField + "\", -" + quantity
                        + "]}, else:0 } } } } ],"
                        + "new: false,"
                        + "fields: {\"basicInfo\": 1},"
                        + "upsert: false})";

        String productJson = mongoOperations.executeCommand(ops).toJson();
        Product product = getProductObj(productJson);
        return getOptional(product);
    }

    @Override
    public void unlock(String sku, String variationId, int quantity)
    {

        Query query = new Query(Criteria.where("sku").is(sku));

        Update update = new Update();
        update.inc(QUANTITY_FIELD, quantity);

        UpdateResult result = mongoOperations.updateFirst(query, update, Product.class);

    }

    private Product getProductObj(String productJson)
    {
        try
        {
            return objectMapper.readValue(productJson, Product.class);
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    private Optional<Product> getOptional(Product product)
    {
        Optional<Product> optionalProduct = Optional.empty();
        if (product != null)
        {
            optionalProduct = Optional.of(product);
        }
        return optionalProduct;
    }
}
