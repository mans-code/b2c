package com.mans.ecommerce.b2c.repository.customer;

import com.mans.ecommerce.b2c.domain.entity.customer.CustomerPaymentInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerPaymentInfoRepository extends MongoRepository<CustomerPaymentInfo, String>
{
}
