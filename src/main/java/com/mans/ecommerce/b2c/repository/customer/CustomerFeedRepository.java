package com.mans.ecommerce.b2c.repository.customer;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.customer.CustomerFeed;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerFeedRepository extends MongoRepository<CustomerFeed, String>
{
    <S extends CustomerFeed> List<S> saveAll(Iterable<S> entities);

    <S extends CustomerFeed> S save(S entity);
}
