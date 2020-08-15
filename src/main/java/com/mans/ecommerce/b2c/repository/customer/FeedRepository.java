package com.mans.ecommerce.b2c.repository.customer;

import com.mans.ecommerce.b2c.domain.entity.customer.Feed;
import com.mans.ecommerce.b2c.repository.customer.custom.FeedRepositoryCustom;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface FeedRepository extends FeedRepositoryCustom, ReactiveMongoRepository<Feed, ObjectId>
{

    @Override <S extends Feed> Mono<S> save(S entity);

    @Override <S extends Feed> Flux<S> saveAll(Iterable<S> entities);
}
