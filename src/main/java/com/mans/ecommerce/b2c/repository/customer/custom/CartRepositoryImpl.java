package com.mans.ecommerce.b2c.repository.customer.custom;

import java.time.Instant;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Mono;
import static org.springframework.data.mongodb.core.query.Criteria.where;

public class CartRepositoryImpl implements CartRepositoryCustom
{

    private final String ID = "_id";

    private final String EXPIRE_DATE = "expireDate";

    private final String ACTIVE = "active";

    private ReactiveMongoTemplate mongoTemplate;

    CartRepositoryImpl(ReactiveMongoTemplate mongoTemplate, ObjectMapper mapper)
    {
        this.mongoTemplate = mongoTemplate;
    }

    @Override public Mono<Boolean> extendsExpirationDateAndGetActivationStatus(ObjectId id, Instant time)
    {
        Query query = new Query();
        query.addCriteria(where(ID).is(id));
        query.fields().exclude(ID).include(ACTIVE);

        Update update = Update.update(EXPIRE_DATE, time);

        FindAndModifyOptions options = FindAndModifyOptions
                                               .options()
                                               .returnNew(true)
                                               .upsert(false)
                                               .remove(false);

        return mongoTemplate.findAndModify(query, update, options, Cart.class)
                            .map(cart -> cart.isActive());
    }

    @Override public Mono<Cart> findAndLock(ObjectId id, Instant time)
    {
        Query query = new Query();
        query.addCriteria(where(ID).is(id));
        query.addCriteria(where(ACTIVE).is(false));

        Update update = new Update();
        update.set(ACTIVE, true);
        update.set(EXPIRE_DATE, time);

        FindAndModifyOptions options = FindAndModifyOptions
                                               .options()
                                               .returnNew(true)
                                               .upsert(false)
                                               .remove(false);

        return mongoTemplate.findAndModify(query, update, options, Cart.class)
                            .cache();
    }

    @Override public Mono<Cart> findAndUnlock(ObjectId id)
    {
        Query query = new Query();
        query.addCriteria(where(ID).is(id));
        query.addCriteria(where(ACTIVE).is(true));

        Update update = new Update();
        update.set(ACTIVE, false);

        FindAndModifyOptions options = FindAndModifyOptions
                                               .options()
                                               .returnNew(true)
                                               .upsert(false)
                                               .remove(false);

        return mongoTemplate.findAndModify(query, update, options, Cart.class)
                            .cache();
    }
}