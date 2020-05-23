package com.mans.ecommerce.b2c.repository.product;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.product.Details;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailsRepository extends MongoRepository<Details, String>
{
    <S extends Details> List<S> saveAll(Iterable<S> entities);

    <S extends Details> S save(S entity);
}
