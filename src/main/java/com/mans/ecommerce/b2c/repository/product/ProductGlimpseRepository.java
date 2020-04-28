package com.mans.ecommerce.b2c.repository.product;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.product.ProductGlimpse;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductGlimpseRepository extends MongoRepository<ProductGlimpse, String>
{
    <S extends ProductGlimpse> List<S> saveAll(Iterable<S> entities);

    <S extends ProductGlimpse> S save(S entity);
}
