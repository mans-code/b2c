package com.mans.ecommerce.b2c.repository.product;

import com.mans.ecommerce.b2c.domain.entity.product.Review;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ReviewRepository extends ReactiveMongoRepository<Review, ObjectId>
{

    @Override <S extends Review> Mono<S> save(S entity);

    @Override <S extends Review> Flux<S> saveAll(Iterable<S> entities);

    Flux<Review> findBySku(String sku, Pageable pageable);

}
