package com.mans.ecommerce.b2c.repository.customer;

import com.mans.ecommerce.b2c.domain.entity.customer.PaymentInfo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PaymentInfoRepository extends ReactiveMongoRepository<PaymentInfo, String>
{
    @Override <S extends PaymentInfo> Mono<S> save(S entity);

    @Override <S extends PaymentInfo> Flux<S> saveAll(Iterable<S> entities);
}
