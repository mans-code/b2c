package com.mans.ecommerce.b2c.repository.customer;

import com.mans.ecommerce.b2c.domain.entity.customer.Customer;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface FeedRepository extends ReactiveMongoRepository<Customer, ObjectId>
{

    @Override <S extends Customer> Mono<S> save(S entity);

    @Override <S extends Customer> Flux<S> saveAll(Iterable<S> entities);
}
