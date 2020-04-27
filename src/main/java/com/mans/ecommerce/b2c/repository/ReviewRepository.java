package com.mans.ecommerce.b2c.repository;

import com.mans.ecommerce.b2c.domain.entity.product.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewRepository extends MongoRepository<Review, String>
{
}
