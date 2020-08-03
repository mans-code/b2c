package com.mans.ecommerce.b2c.service;

import java.util.ArrayList;
import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import com.mans.ecommerce.b2c.domain.logic.CartLogic;
import com.mans.ecommerce.b2c.repository.product.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class CheckoutService
{

    private ProductRepository productRepository;

    private CartLogic carService;

    CheckoutService(ProductRepository productRepository, CartLogic carService)
    {
        this.productRepository = productRepository;
        this.carService = carService;
    }

    public List<ProductInfo> lock(Cart cart)
    {
        carService.avoidUnlock(cart);
        List<ProductInfo> lockedProductInfos = new ArrayList<>();
        for (ProductInfo productInfo : cart.getProductInfos())
        {
            int locked = lock(cart, productInfo);
            ProductInfo lockedProductInfo = getLockProductInfo(productInfo, locked);
            lockedProductInfos.add(lockedProductInfo);
        }
        return lockedProductInfos;
    }

    public int lock(Cart cart, ProductInfo cartProduct)
    {
        carService.avoidUnlock(cart);
        String sku = cartProduct.getSku();
        String variationId = cartProduct.getVariationId();
        String cartId = cart.getId();
        int toLock = cartProduct.getQuantity();

        return productRepository.lock(sku, variationId, cartId, toLock);
    }

    public int partialLock(Cart cart, ProductInfo cartProduct, int toLock)
    {
        carService.avoidUnlock(cart);
        String sku = cartProduct.getSku();
        String variationId = cartProduct.getVariationId();
        String cartId = cart.getId();
        int newQuantity = cartProduct.getQuantity();
        return productRepository.partialLock(sku, variationId, cartId, toLock, newQuantity);
    }

    public void unlock(Cart cart)
    {
        carService.avoidUnlock(cart);
        for (ProductInfo productInfo : cart.getProductInfos())
        {
            unlock(cart, productInfo);
        }
    }

    public void unlock(Cart cart, ProductInfo cartProduct)
    {
        carService.avoidUnlock(cart);
        String sku = cartProduct.getSku();
        String variationId = cartProduct.getVariationId();
        int quantityToUnlock = cartProduct.getQuantity();
        String cartId = cart.getId();
        productRepository.unlock(sku, variationId, cartId, quantityToUnlock);
    }

    public void unlock(Cart cart, ProductInfo cartProduct, int toUnlock)
    {
        carService.avoidUnlock(cart);
        String sku = cartProduct.getSku();
        String variationId = cartProduct.getVariationId();
        String cartId = cart.getId();
        int newQuantity = cartProduct.getQuantity();
        productRepository.partialUnlock(sku, variationId, cartId, toUnlock, newQuantity);
    }

    private ProductInfo getLockProductInfo(ProductInfo productInfo, int locked)
    {
        return ProductInfo
                       .builder()
                       .sku(productInfo.getSku())
                       .variationId(productInfo.getVariationId())
                       .quantity(locked)
                       .build();
    }
}
