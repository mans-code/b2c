package com.mans.ecommerce.b2c.repository.product;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.product.ProductDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductDetailsRepository extends MongoRepository<ProductDetails, String>
{
    <S extends ProductDetails> List<S> saveAll(Iterable<S> entities);

    <S extends ProductDetails> S save(S entity);
}
