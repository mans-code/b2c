package com.mans.ecommerce.b2c.server.eventListener;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.financial.Order;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import com.mans.ecommerce.b2c.server.eventListener.entity.OrderEvent;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class OrderListeners
{

    private ApplicationEventPublisher publisher;

    OrderListeners(ApplicationEventPublisher publisher)
    {
        this.publisher = publisher;
    }

    @Async
    @EventListener
    void saveOrder(OrderEvent orderEvent)
    {
        Order order = orderEvent.getOrder();

        List<ProductInfo> productInfos = order.getDetail().getProductInfos();
        ObjectId customerId = order.getCustomerId();
        //publisher.publishEvent(new BoughtProductsEvent(customerId, productInfos));
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
