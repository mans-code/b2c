package com.mans.ecommerce.b2c.repository.financial;

import com.mans.ecommerce.b2c.domain.entity.financial.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<Order, String>
{
    <S extends Order> S save(S entity);
}
