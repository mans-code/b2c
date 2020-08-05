package com.mans.ecommerce.b2c.server.eventListener;

import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import com.mans.ecommerce.b2c.domain.exception.SystemConstraintViolation;
import com.mans.ecommerce.b2c.server.eventListener.entity.UnlockCartEvent;
import com.mans.ecommerce.b2c.server.eventListener.entity.UnlockProductEvent;
import com.mans.ecommerce.b2c.server.eventListener.entity.UnlockProductPartiallyEvent;
import com.mans.ecommerce.b2c.service.CartService;
import com.mans.ecommerce.b2c.service.ProductService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

public class UnlockListeners
{

    private ProductService productService;

    private CartService cartService;

    UnlockListeners(ProductService productService, CartService cartService)
    {
        this.productService = productService;
        this.cartService = cartService;
    }

    @Async
    @EventListener
    void unlockCart(UnlockCartEvent event)
    {
        String cartId = event.getCartId();
        boolean stillActive = cartService.extendsExpirationDateAndGetActivationStatus(cartId);
        if (!stillActive)
        {
            return;
        }
        event.getProductInfos()
             .forEach(productInfo -> unlock(productInfo, cartId));
    }

    @Async
    @EventListener
    void unlockProduct(UnlockProductEvent event)
    {
        String cartId = event.getCartId();
        boolean stillActive = cartService.extendsExpirationDateAndGetActivationStatus(cartId);
        if (!stillActive)
        {
            return;
        }

        ProductInfo productInfo = event.getProductInfo();
        unlock(productInfo, cartId);
    }

    @Async
    @EventListener
    void unlockProductPartially(UnlockProductPartiallyEvent event)
    {
        String cartId = event.getCartId();
        boolean stillActive = cartService.extendsExpirationDateAndGetActivationStatus(cartId);
        if (!stillActive)
        {
            return;
        }

        ProductInfo productInfo = event.getProductInfo();
        int toUnlock = event.getToUnlock();
        int newReservedQuantity = event.getNewReservedQuantity();
        unlock(productInfo, cartId, toUnlock, newReservedQuantity);
    }

    private void unlock(ProductInfo productInfo, String cartId)
    {
        unlock(productInfo, cartId, productInfo.getQuantity(), -1);
    }

    private void unlock(ProductInfo productInfo, String cartId, int toUnlock, int newReservedQuantity)
    {
        String sku = productInfo.getSku();
        String variationId = productInfo.getVariationId();
        boolean pass;
        if (newReservedQuantity != -1)
        {
            pass = productService.unlock(sku, variationId, cartId, toUnlock);
        }
        else
        {
            pass = productService.partialUnlock(sku, variationId, cartId, toUnlock, newReservedQuantity);
        }

        if (!pass)
        {
            new SystemConstraintViolation(
                    String.format("couldn't unlock sku=%s variationId=%s toUnlock=%d newReservedQuantity=%d",
                                  sku, variationId, toUnlock, newReservedQuantity));
        }

    }
}
