package com.mans.ecommerce.b2c.repository.product;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.product.Product;
import com.mans.ecommerce.b2c.repository.product.CustomRepository.ProductRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String>, ProductRepositoryCustom
{

    <S extends Product> List<S> saveAll(Iterable<S> entities);

    <S extends Product> S save(S entity);

    @Query(value="{ 'sku' : ?0 }", fields="{ 'firstname' : 1, 'lastname' : 1}")
    <S extends Product> S getProductToAddToCart(String sku);

}
