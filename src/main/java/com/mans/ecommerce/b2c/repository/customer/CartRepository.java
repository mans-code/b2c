package com.mans.ecommerce.b2c.repository.customer;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.product.QAndA;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends MongoRepository<Cart, String>
{
    <S extends Cart> List<S> saveAll(Iterable<S> entities);

    <S extends Cart> S save(S entity);

}
