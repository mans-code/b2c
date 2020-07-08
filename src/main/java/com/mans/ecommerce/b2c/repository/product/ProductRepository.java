package com.mans.ecommerce.b2c.repository.product;

import java.util.List;
import java.util.Optional;

import com.mans.ecommerce.b2c.domain.entity.product.Product;
import com.mans.ecommerce.b2c.domain.entity.product.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ProductRepository extends MongoRepository<Product, String>
{

    <S extends Product> List<S> saveAll(Iterable<S> entities);

    <S extends Product> S save(S entity);

    @Query(value="{ 'id' : ?0 }", fields="{ 'id' : 1, 'basicInfo' : 1}")
    Optional<Product> findProductBasicInfo(String productId);

    void unLock(String productId, int quantityToUnlock);
}
