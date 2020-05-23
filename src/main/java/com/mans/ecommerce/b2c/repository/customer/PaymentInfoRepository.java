package com.mans.ecommerce.b2c.repository.customer;

import com.mans.ecommerce.b2c.domain.entity.customer.PaymentInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentInfoRepository extends MongoRepository<PaymentInfo, String>
{
}
