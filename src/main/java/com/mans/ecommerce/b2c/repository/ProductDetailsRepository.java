package com.mans.ecommerce.b2c.repository;

import com.mans.ecommerce.b2c.domain.entity.product.ProductDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductDetailsRepository extends MongoRepository<ProductDetails, String>
{
}
