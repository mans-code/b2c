package com.mans.ecommerce.b2c.server.eventListener.entity;

import com.mans.ecommerce.b2c.domain.entity.financial.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class OrderEvent
{
    private Order order;
}
