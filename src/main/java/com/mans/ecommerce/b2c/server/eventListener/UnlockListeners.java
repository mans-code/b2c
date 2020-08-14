package com.mans.ecommerce.b2c.server.eventListener;

import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import com.mans.ecommerce.b2c.domain.exception.SystemConstraintViolation;
import com.mans.ecommerce.b2c.server.eventListener.entity.UnlockCartEvent;
import com.mans.ecommerce.b2c.server.eventListener.entity.UnlockProductEvent;
import com.mans.ecommerce.b2c.server.eventListener.entity.UnlockProductPartiallyEvent;
import com.mans.ecommerce.b2c.service.CartService;
import com.mans.ecommerce.b2c.service.ProductService;
import org.bson.types.ObjectId;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import reactor.core.publisher.Mono;

public class UnlockListeners
{

    private ProductService productService;

    private CartService cartService;
//
//    UnlockListeners(ProductService productService, CartService cartService)
//    {
//        this.productService = productService;
//        this.cartService = cartService;
//    }
//
//    @Async
//    @EventListener
//    void unlockCart(UnlockCartEvent event)
//    {
//        ObjectId cartId = event.getCartId();
//        boolean stillActive = cartService.extendsExpirationDateAndGetActivationStatus(cartId);
//        if (!stillActive)
//        {
//            return;
//        }
//        event.getProductInfos()
//             .forEach(productInfo -> unlock(productInfo, cartId));
//    }
//
//    @Async
//    @EventListener
//    void unlockProduct(UnlockProductEvent event)
//    {
//        ObjectId cartId = event.getCartId();
//        boolean stillActive = cartService.extendsExpirationDateAndGetActivationStatus(cartId);
//        if (!stillActive)
//        {
//            return;
//        }
//
//        ProductInfo productInfo = event.getProductInfo();
//        unlock(productInfo, cartId);
//    }
//
//    @Async
//    @EventListener
//    void unlockProductPartially(UnlockProductPartiallyEvent event)
//    {
//        ObjectId cartId = event.getCartId();
//        boolean stillActive = cartService.extendsExpirationDateAndGetActivationStatus(cartId);
//        if (!stillActive)
//        {
//            return;
//        }
//
//        ProductInfo productInfo = event.getProductInfo();
//        int toUnlock = event.getToUnlock();
//        int newReservedQuantity = event.getNewReservedQuantity();
//        unlock(productInfo, cartId, toUnlock, newReservedQuantity);
//    }
//
//    private void unlock(ProductInfo productInfo, ObjectId cartId)
//    {
//        unlock(productInfo, cartId, productInfo.getQuantity(), -1);
//    }
//
//    private void unlock(ProductInfo productInfo, ObjectId cartId, int toUnlock, int newReservedQuantity)
//    {
//        String sku = productInfo.getSku();
//        String variationId = productInfo.getVariationId();
//        Mono<Boolean> passMono;
//        if (newReservedQuantity != -1)
//        {
//            passMono = productService.unlock(sku, variationId, cartId, toUnlock);
//        }
//        else
//        {
//            passMono = productService.partialUnlock(sku, variationId, cartId, toUnlock, newReservedQuantity);
//        }
//
//        throwIfUnlocked(passMono, productInfo, cartId, toUnlock, newReservedQuantity);
//    }
//
//    private void throwIfUnlocked(Mono<Boolean> passMono, ProductInfo productInfo, ObjectId cartId, int toUnlock, int newReservedQuantity)
//    {
//    }
//
//    private void throwIfUnlocked(Mono<Boolean> passMono, ObjectId cartId, int toUnlock, int newReservedQuantity)
//    {
//        passMono.doOnError(ex -> throwException(productInfo, cartId, toUnlock, newReservedQuantity, ex));
//
//        passMono.doOnSuccess(res -> {
//            if (res == null || !res)
//            {
//                throwException(productInfo, cartId, toUnlock, newReservedQuantity);
//            }
//    }
//
//
//    private void throwException(ObjectId cartId, int toUnlock, int newReservedQuantity, Throwable ex)
//    {
//        new SystemConstraintViolation(
//                String.format("couldn't unlock sku=%s variationId=%s toUnlock=%d newReservedQuantity=%d",
//                              sku, variationId, toUnlock, newReservedQuantity));
//    }
//    }
//
//    private void throwException(ObjectId cartId, int toUnlock, int newReservedQuantity)
//    {
//        throwException(cartId, toUnlock, newReservedQuantity, null);
//    }
//
//    private void throwIfUnlocked(Mono<Boolean> passMono)
//    {
//        passMono.doOnSuccess(unlocked -> {
//            if (unlocked == null || !unlocked)
//            {
//                throwException();
//
//            }
//        })
//    }
}
