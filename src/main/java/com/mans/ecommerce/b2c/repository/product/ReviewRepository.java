package com.mans.ecommerce.b2c.repository.product;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.product.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String>
{
    <S extends Review> List<S> saveAll(Iterable<S> entities);

    <S extends Review> S save(S entity);


}
