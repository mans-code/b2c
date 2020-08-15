package com.mans.ecommerce.b2c.service;

import com.mans.ecommerce.b2c.domain.entity.financial.Order;
import com.mans.ecommerce.b2c.repository.financial.OrderRepository;
import com.mans.ecommerce.b2c.server.eventListener.entity.OrderEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OrderService
{

    private OrderRepository orderRepository;

    private ApplicationEventPublisher publisher;

    OrderService(
            OrderRepository orderRepository,
            ApplicationEventPublisher publisher)
    {
        this.publisher = publisher;
        this.orderRepository = orderRepository;
    }

    public Mono<Order> save(Order order)
    {
        publisher.publishEvent(new OrderEvent(order));
        orderRepository.save(order);
        return orderRepository.save(order);
    }

}
