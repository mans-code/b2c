package com.mans.ecommerce.b2c.repository.product;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.product.Glimpse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlimpseRepository extends MongoRepository<Glimpse, String>
{
    <S extends Glimpse> List<S> saveAll(Iterable<S> entities);

    <S extends Glimpse> S save(S entity);
}
