package com.mans.ecommerce.b2c.repository.customer;

import java.util.List;
import java.util.Optional;

import com.mans.ecommerce.b2c.domain.entity.customer.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, String>
{
    Optional<Customer> findByUsername(String s);
}
