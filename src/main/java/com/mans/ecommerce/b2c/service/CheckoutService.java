package com.mans.ecommerce.b2c.service;

import java.util.ArrayList;
import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import com.mans.ecommerce.b2c.repository.product.ProductRepository;
import com.mans.ecommerce.b2c.server.eventListener.entity.UnlockCartEvent;
import com.mans.ecommerce.b2c.server.eventListener.entity.UnlockProductEvent;
import com.mans.ecommerce.b2c.server.eventListener.entity.UnlockProductPartiallyEvent;
import com.mans.ecommerce.b2c.utill.ProductLockErrorInfo;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class CheckoutService
{

    private ProductRepository productRepository;

    private CartService carService;

    private ApplicationEventPublisher publisher;

    CheckoutService(
            ProductRepository productRepository,
            CartService carService,
            ApplicationEventPublisher publisher)
    {
        this.productRepository = productRepository;
        this.carService = carService;
        this.publisher = publisher;
    }

    public List<ProductLockErrorInfo> lock(Cart cart)
    {
        carService.avoidUnlock(cart);

        List<ProductLockErrorInfo> lockInfo = new ArrayList<>();
        for (ProductInfo productInfo : cart.getProductInfos())
        {
            int lockedQuantity = lock(cart, productInfo);
            if (productInfo.getQuantity() != lockedQuantity)
            {
                productInfo.setQuantity(lockedQuantity);
                ProductLockErrorInfo productLockInfo = getProductLockInfo(productInfo, lockedQuantity);
                lockInfo.add(productLockInfo);
            }
        }
        return lockInfo;
    }

    public int lock(Cart cart, ProductInfo cartProduct)
    {
        carService.avoidUnlock(cart);
        String sku = cartProduct.getSku();
        String variationId = cartProduct.getVariationId();
        ObjectId cartId = cart.getIdObj();
        int toLock = cartProduct.getQuantity();

        return productRepository.lock(sku, variationId, cartId, toLock);
    }

    public int lock(Cart cart, ProductInfo cartProduct, int toLock)
    {
        carService.avoidUnlock(cart);
        String sku = cartProduct.getSku();
        String variationId = cartProduct.getVariationId();
        ObjectId cartId = cart.getIdObj();
        int newReservedQuantity = cartProduct.getQuantity();
        return productRepository.partialLock(sku, variationId, cartId, toLock, newReservedQuantity);
    }

    public void unlock(ObjectId cartId, List<ProductInfo> productInfos)
    {
        publisher.publishEvent(new UnlockCartEvent(cartId, productInfos));
    }

    public void unlock(ObjectId cartId, ProductInfo cartProduct)
    {
        publisher.publishEvent(new UnlockProductEvent(cartId, cartProduct));
    }

    public void unlock(ObjectId cartId, ProductInfo cartProduct, int toUnlock, int newReservedQuantity)
    {
        publisher.publishEvent(new UnlockProductPartiallyEvent(cartId, cartProduct, toUnlock, newReservedQuantity));
    }

    private ProductLockErrorInfo getProductLockInfo(ProductInfo productInfo, int lockedQuantity)
    {
        return ProductLockErrorInfo
                       .builder()
                       .sku(productInfo.getSku())
                       .title(productInfo.getTitle())
                       .lockedQuantity(lockedQuantity)
                       .requestedQuantity(productInfo.getQuantity())
                       .build();
    }

}
