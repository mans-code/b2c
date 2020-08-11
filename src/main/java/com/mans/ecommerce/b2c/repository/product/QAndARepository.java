package com.mans.ecommerce.b2c.repository.product;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.product.Product;
import com.mans.ecommerce.b2c.domain.entity.product.QAndA;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface QAndARepository extends ReactiveMongoRepository<QAndA, ObjectId>
{

    @Override <S extends QAndA> Mono<S> save(S entity);

    @Override <S extends QAndA> Flux<S> saveAll(Iterable<S> entities);

    Flux<QAndA> findBySku(String sku, Pageable pageable);
}
