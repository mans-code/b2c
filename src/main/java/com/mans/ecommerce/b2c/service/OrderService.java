package com.mans.ecommerce.b2c.service;

import com.mans.ecommerce.b2c.domain.entity.financial.Order;
import com.mans.ecommerce.b2c.repository.financial.OrderRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OrderService
{

    private OrderRepository orderRepository;

    OrderService(OrderRepository orderRepository)
    {
        this.orderRepository = orderRepository;
    }

    public Mono<Order> save(Order order)
    {
        return orderRepository.save(order);
    }

}
