package com.mans.ecommerce.b2c.repository.product;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.product.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends PagingAndSortingRepository<Review, String>
{
    <S extends Review> List<S> saveAll(Iterable<S> entities);

    <S extends Review> S save(S entity);

    @Query("{ 'sku' : ?0}")
    List<Review> findBySku(String sku, Pageable pageable);

}
