package com.mans.ecommerce.b2c.repository.customer;

import java.util.Optional;

import com.mans.ecommerce.b2c.domain.entity.customer.Customer;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, ObjectId>
{

    Optional<Customer> findById(ObjectId objectId);

    Optional<Customer> findByUsername(String username);

    Optional<Customer> findByToken(String token);

    boolean existsByUsername(String s);

    @Query(value = "{ 'id' : ?0 }", fields = "{ 'shippingAddresses' : 1, 'id' : 0}")
    Optional<Customer> getShippingAddresses(ObjectId id);
}
