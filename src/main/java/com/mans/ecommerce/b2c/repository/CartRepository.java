package com.mans.ecommerce.b2c.repository;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartRepository extends MongoRepository<Cart, String>
{
}
