package com.mans.ecommerce.b2c.repository.product;

import com.mans.ecommerce.b2c.domain.entity.product.Product;
import com.mans.ecommerce.b2c.repository.product.Custom.ProductVariation;
import com.mans.ecommerce.b2c.repository.product.Custom.ProductLocking;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, ObjectId>, ProductLocking, ProductVariation
{

    @Override <S extends Product> Mono<S> save(S entity);

    @Override <S extends Product> Flux<S> saveAll(Iterable<S> entities);

    Mono<Product> getBySku(String sku);
}
