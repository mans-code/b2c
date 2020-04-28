package com.mans.ecommerce.b2c.repository.product;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.product.ProductQAndA;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductQAndARepository extends MongoRepository<ProductQAndA, String>
{
    <S extends ProductQAndA> List<S> saveAll(Iterable<S> entities);

    <S extends ProductQAndA> S save(S entity);

}
