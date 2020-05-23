package com.mans.ecommerce.b2c.repository.customer;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.customer.Feed;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedRepository extends MongoRepository<Feed, String>
{
    <S extends Feed> List<S> saveAll(Iterable<S> entities);

    <S extends Feed> S save(S entity);
}
