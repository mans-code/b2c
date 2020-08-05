package com.mans.ecommerce.b2c.repository.customer.custom;

import java.util.Date;
import java.util.Objects;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.exception.SystemConstraintViolation;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import static org.springframework.data.mongodb.core.query.Criteria.where;

public class CartRepositoryImpl implements CartRepositoryCustom
{

    private final String ID = "id";

    private final String EXPIRE_DATE = "expireDate";

    private MongoOperations mongoOperations;

    @Override public boolean extendsExpirationDateAndGetActivationStatus(String id, Date date)
    {
        Query query = new Query();
        query.addCriteria(where(ID).is(id));
        query.fields().exclude(ID).include(EXPIRE_DATE);

        Update update = Update.update(EXPIRE_DATE, date);

        FindAndModifyOptions options = FindAndModifyOptions
                                               .options()
                                               .returnNew(true)
                                               .upsert(false)
                                               .remove(false);

        Cart cart = mongoOperations.findAndModify(query, update, options, Cart.class);

        if (Objects.isNull(cart))
        {
            String message = "Couldn't find cart, cartId=%s";
            throw new SystemConstraintViolation(String.format(message, id));
        }

        return cart.isActive();
    }
}
