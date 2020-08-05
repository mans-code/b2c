package com.mans.ecommerce.b2c.server.eventListener;

import com.mans.ecommerce.b2c.domain.entity.financial.Order;
import com.mans.ecommerce.b2c.server.eventListener.entity.OrderEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class OrderListeners
{

    @Async
    @EventListener
    void saveOrder(OrderEvent orderEvent)
    {
        Order customer = orderEvent.getOrder();
        // handle event
    }

    @Async
    @EventListener
    void removeReservation(OrderEvent orderEvent)
    {
        Order customer = orderEvent.getOrder();
        // handle event
    }

    @Async
    @EventListener
    void startShipping(OrderEvent orderEvent)
    {
        Order customer = orderEvent.getOrder();
        // handle event
    }
}
