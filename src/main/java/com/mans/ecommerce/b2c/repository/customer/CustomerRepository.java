package com.mans.ecommerce.b2c.repository.customer;

import java.util.Optional;

import com.mans.ecommerce.b2c.domain.entity.customer.Customer;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, String>
{

    @Override
    Optional<Customer> findById(String s);

    Optional<Customer> findByUsername(String username);

    Optional<Customer> findByToken(String token);

    boolean existsByUsername(String s);

    @Query(value = "{ 'id' : ?0 }", fields = "{ 'shippingAddresses' : 1, 'id' : 0}")
    Optional<Customer> getShippingAddresses(String customerId);
}
