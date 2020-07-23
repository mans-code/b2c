package com.mans.ecommerce.b2c.repository.product.CustomRepository;

import java.util.Optional;

import com.mans.ecommerce.b2c.domain.entity.product.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepositoryCustom
{
    Optional<Product> lockAndProjectBasicInfoAndPrevAvailability(String productId, int quantity);

    boolean unlock(String productId, int quantity);
}
