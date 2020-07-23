package com.mans.ecommerce.b2c.repository.customer;

import java.util.Optional;

import com.mans.ecommerce.b2c.domain.entity.customer.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, String>
{

    @Override
    Optional<Customer> findById(String s);

    Optional<Customer> findByUsername(String s);

    boolean existsByUsername(String s);
}
