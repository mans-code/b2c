package com.mans.ecommerce.b2c.repository.customer.custom;

import java.lang.ref.PhantomReference;
import java.time.Instant;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.exception.SystemConstraintViolation;
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

        Mono<Cart> cartMono = mongoTemplate.findAndModify(query, update, options, Cart.class);

        return getActivationStatus(cartMono, id);
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

    private Mono<Boolean> getActivationStatus(Mono<Cart> cartMono, ObjectId id)
    {
        throwIfNull(cartMono);
        return cartMono.flatMap(cart -> Mono.just(cart.isActive()));
    }

    private void throwIfNull(Mono<Cart> cartMono)
    {
        cartMono.doOnSuccess(cart -> {
            if (Objects.isNull(cart))
            {
                throw new SystemConstraintViolation(String.format(ERROR, cart.getId()));
            }
        });
    }
}
