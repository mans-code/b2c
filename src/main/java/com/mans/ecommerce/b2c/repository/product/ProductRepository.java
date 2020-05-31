package com.mans.ecommerce.b2c.repository.product;

import java.util.Optional;

import com.mans.ecommerce.b2c.domain.entity.product.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ProductRepository extends MongoRepository<Product, String>
{

    @Query(value="{ 'id' : ?0 }", fields="{ 'id' : 1, 'basicInfo' : 1}")
    Optional<Product> findProductBasicInfo(String productId);

    void unLock(String productId, int quantityToUnlock);
}
