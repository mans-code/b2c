package com.mans.ecommerce.b2c.repository.customer;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.repository.customer.custom.CartRepositoryCustom;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CartRepository  extends CartRepositoryCustom, ReactiveMongoRepository<Cart, ObjectId>
{

    @Override <S extends Cart> Mono<S> save(S entity);

    @Override <S extends Cart> Flux<S> saveAll(Iterable<S> entities);

    @Override Mono<Boolean> existsById(ObjectId objectId);

}
