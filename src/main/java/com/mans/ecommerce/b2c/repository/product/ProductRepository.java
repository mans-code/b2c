package com.mans.ecommerce.b2c.repository.product;

import java.util.List;
import java.util.Optional;

import com.mans.ecommerce.b2c.domain.entity.product.Product;
import com.mans.ecommerce.b2c.repository.product.Custom.ProductRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String>, ProductRepositoryCustom
{

    <S extends Product> List<S> saveAll(Iterable<S> entities);

    <S extends Product> S save(S entity);

    @Query(value = "{ 'sku' : ?0 }", fields = "{ 'basicInfo' : 1, 'availability' : 1, 'dSku' : 1}")
    Optional<Product> getProductToAddToCart(String sku);

    Optional<Product> getBySku(String sku);
}
