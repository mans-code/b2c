package com.mans.ecommerce.b2c.server.eventListener;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.exception.SystemConstraintViolation;
import com.mans.ecommerce.b2c.server.eventListener.entity.CartSavingEvent;
import com.mans.ecommerce.b2c.service.CartService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class CartListeners
{

    private CartService cartService;

    CartListeners(CartService cartService)
    {
        this.cartService = cartService;
    }

    @Async
    @EventListener
    void saveCart(CartSavingEvent event)
    {
        Cart cart = event.getCart();
        Cart saved = cartService.syncSave(cart);
        if (saved == null)
        {
            new SystemConstraintViolation(String.format("couldn't save cart id=%s", cart.getId()));
        }
    }
}
