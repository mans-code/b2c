package com.mans.ecommerce.b2c.service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.mans.ecommerce.b2c.controller.utills.dto.ProductInfoDto;
import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.product.Product;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import com.mans.ecommerce.b2c.domain.exception.ResourceNotFoundException;
import com.mans.ecommerce.b2c.repository.product.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService
{
    private final int FIVE_MINUTES = 300000;

    private ProductRepository productRepository;

    ProductService(ProductRepository productRepository)
    {
        this.productRepository = productRepository;
    }

    public Optional<Product> findById(String id)
    {
        return productRepository.findById(id);
    }

    public Product findByIdOrElseThrow(String id)
    {
        Optional<Product> optionalDetails = findById(id);

        if (!optionalDetails.isPresent())
        {
            throw new ResourceNotFoundException(String.format(" couldn't find product  with id = %s", id));
        }

        return optionalDetails.get();
    }

    public void unlock(Cart cart, List<ProductInfo> productInfoList)
    {
        for (ProductInfo productInfo : productInfoList)
        {
            unLock(cart, productInfo);
        }
    }

    public void unLock(Cart cart, ProductInfo productInfo)
    {
        if (cart.isActive() && expireIn5MinsOrLess(cart.getExpireDate()))
        {
            return;
        }

        if (!productInfo.isLocked())
        {
            return;
        }

        String productId = productInfo.getProductId();
        int quantityToUnlock = productInfo.getQuantity();
        //productRepository.unLock(productId, quantityToUnlock);
    }

    public void lock(ProductInfo productInfo)
    {
        String productId = productInfo.getProductId();
        int quantityToLock = productInfo.getQuantity();
        //has only 16 of these available
        //find&Modfiy
    }

    public ProductInfo getProductInfoOrElseThrow(ProductInfoDto productInfoDto)
    {

        String productId = productInfoDto.getProductId();
        Optional<Product> productOptional = productRepository.findProductBasicInfo(productId);
        if (!productOptional.isPresent())
        {
            throw new ResourceNotFoundException(String.format("Couldn't find product with id = %s", productId));
        }

        ProductInfo productInfo = mapProductToProductInfo(productOptional.get());
        return productInfo;
    }

    private boolean expireIn5MinsOrLess(Date expireDate)
    {
        long initTime = expireDate.getTime();
        long now = Instant.now().toEpochMilli();
        return now - initTime <= FIVE_MINUTES;
    }

    private ProductInfo mapProductToProductInfo(Product product)
    {
        ProductInfo productInfo = new ProductInfo();

        productInfo.setProductId(product.getId());
        productInfo.setTitle(product.getBasicInfo().getTitle());
        productInfo.setPrice(product.getBasicInfo().getPriceGlimpse());
        productInfo.setImageUrl(product.getBasicInfo().getMainImageUrl());

        return productInfo;
    }
}
