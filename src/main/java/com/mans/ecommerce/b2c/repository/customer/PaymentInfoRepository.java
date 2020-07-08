package com.mans.ecommerce.b2c.repository.customer;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.customer.Feed;
import com.mans.ecommerce.b2c.domain.entity.customer.PaymentInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentInfoRepository extends MongoRepository<PaymentInfo, String>
{
    <S extends PaymentInfo> List<S> saveAll(Iterable<S> entities);

    <S extends PaymentInfo> S save(S entity);
}
