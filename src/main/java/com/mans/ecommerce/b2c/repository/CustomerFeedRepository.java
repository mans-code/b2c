package com.mans.ecommerce.b2c.repository;

import com.mans.ecommerce.b2c.domain.entity.customer.Feed;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerFeedRepository extends MongoRepository<Feed, String>
{
}
