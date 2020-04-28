package com.mans.ecommerce.b2c.repository.customer;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.customer.CustomerCart;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerCartRepository extends MongoRepository<CustomerCart, String>
{
    <S extends CustomerCart> List<S> saveAll(Iterable<S> entities);

    <S extends CustomerCart> S save(S entity);
}
