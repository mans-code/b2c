package com.mans.ecommerce.b2c.repository;

import java.util.Optional;

import com.mans.ecommerce.b2c.domain.entity.customer.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String>
{
    Optional<Customer> findByUsername(String username);
}
