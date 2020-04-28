package com.mans.ecommerce.b2c.repository.product;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.product.ProductReview;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductReviewRepository extends MongoRepository<ProductReview, String>
{
    <S extends ProductReview> List<S> saveAll(Iterable<S> entities);

    <S extends ProductReview> S save(S entity);
}
