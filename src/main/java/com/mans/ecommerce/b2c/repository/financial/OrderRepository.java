package com.mans.ecommerce.b2c.repository.financial;

import com.mans.ecommerce.b2c.domain.entity.financial.Order;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface OrderRepository extends ReactiveMongoRepository<Order, String>
{
    @Override <S extends Order> Mono<S> save(S entity);

    @Override <S extends Order> Flux<S> saveAll(Iterable<S> entities);
}
