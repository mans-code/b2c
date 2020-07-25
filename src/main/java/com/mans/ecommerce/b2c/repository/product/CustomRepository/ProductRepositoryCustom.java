package com.mans.ecommerce.b2c.repository.product.CustomRepository;

import java.util.Optional;

import com.mans.ecommerce.b2c.domain.entity.product.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepositoryCustom
{
    int lock(String productSku, String availabilitySku, int quantity);

    void unlock(String productSku, String availabilitySku, int quantity);
}
