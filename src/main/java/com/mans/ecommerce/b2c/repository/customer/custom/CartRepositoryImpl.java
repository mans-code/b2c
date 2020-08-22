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

    private final String ID = "id";

    private final String EXPIRE_DATE = "expireDate";

    private final String ACTIVE = "active";

    private final String LOCKING = "locking";

    private final String ERROR = "Couldn't find cart, cartId=%s";

    private ReactiveMongoTemplate mongoTemplate;

    private ObjectMapper mapper;

    CartRepositoryImpl(ReactiveMongoTemplate mongoTemplate, ObjectMapper mapper)
    {
        this.mongoTemplate = mongoTemplate;
    }

    @Override public Mono<Boolean> extendsExpirationDateAndGetActivationStatus(ObjectId id, Instant time)
    {
        Query query = new Query();
        query.addCriteria(where(ID).is(id));
        query.fields().exclude(ID).include(ACTIVE);
        Update update = Update.update(EXPIRE_DATE, time.getEpochSecond());

        FindAndModifyOptions options = FindAndModifyOptions
                                               .options()
                                               .returnNew(true)
                                               .upsert(false)
                                               .remove(false);

        return mongoTemplate.findAndModify(query, update, options, Cart.class)
                            .flatMap(cart -> Mono.just(cart.isActive()));
    }

    @Override public Mono<Cart> findAndLock(ObjectId id, Instant time)
    {
        Query query = new Query();
        query.addCriteria(where(ID).is(id));
        query.addCriteria(where(ACTIVE).is(false));

        Update update = new Update();
        update.set(ACTIVE, true);
        update.set(EXPIRE_DATE, time.getEpochSecond());

        FindAndModifyOptions options = FindAndModifyOptions
                                               .options()
                                               .returnNew(false)
                                               .upsert(false)
                                               .remove(false);

        return mongoTemplate.findAndModify(query, update, options, Cart.class);
    }

}
