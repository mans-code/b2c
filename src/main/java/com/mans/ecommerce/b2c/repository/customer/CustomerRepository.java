package com.mans.ecommerce.b2c.repository.customer;

import com.mans.ecommerce.b2c.domain.entity.customer.Customer;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerRepository extends ReactiveMongoRepository<Customer, ObjectId>
{



    Mono<Customer> findById(ObjectId objectId);


    boolean existsByUsername(String s);

    @Query(value = "{ 'username' : ?0 }", fields = "{ 'username' : 1, 'password' : 1}")
    Mono<Customer> findToLogin(String usernameS);

    @Query(value = "{ 'id' : ?0 }", fields = "{ 'shippingAddresses' : 1, 'id' : 0}")
    Mono<Customer> getShippingAddresses(ObjectId id);

}
