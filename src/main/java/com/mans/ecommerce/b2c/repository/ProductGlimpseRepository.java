package com.mans.ecommerce.b2c.repository;

import com.mans.ecommerce.b2c.domain.entity.product.ProductGlimpse;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductGlimpseRepository extends MongoRepository<ProductGlimpse, String>
{

}
