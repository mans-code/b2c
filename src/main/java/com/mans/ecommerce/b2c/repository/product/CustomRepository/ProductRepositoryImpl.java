package com.mans.ecommerce.b2c.repository.product.CustomRepository;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mans.ecommerce.b2c.domain.entity.product.Product;
import com.mans.ecommerce.b2c.domain.exception.ResourceNotFoundException;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
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
    public Optional<Product> lockAndProjectBasicInfoAndPrevAvailability(String productId, int quantity)
    {

        String ops =
                "db.products.findAndModify("
                        + "{query:{ _id :ObjectId(\"" + productId + "\") },"
                        + "update: [{ $set: { \"availability.numUnitsAvailable\": "
                        + "{ $cond: { if: { $gt: [\"$availability.numUnitsAvailable\"," + quantity
                        + "] }, then: {$sum:[ \"$availability.numUnitsAvailable\", -" + quantity
                        + "]}, else:0 } } } } ],"
                        + "new: false,"
                        + "fields: {\"basicInfo\": 1},"
                        + "upsert: false})";

        String productJson = mongoOperations.executeCommand(ops).toJson();
        Product product = getProductObj(productJson);
        return getOptional(product);
    }

    @Override
    public boolean unlock(String productId, int quantity)
    {
        ObjectId mongoId = new ObjectId(productId);
        Query query = new Query(Criteria.where("id").is(mongoId));

        Update update = new Update();
        update.inc(QUANTITY_FIELD, quantity);

        UpdateResult result = mongoOperations.updateFirst(query, update, Product.class);
        if (result.getMatchedCount() == 0)
        {
            throw new ResourceNotFoundException(String.format(PRODUCT_NOT_FOUNT_TEMPLATE, productId));
        }
        return result.getModifiedCount() == 1;
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
