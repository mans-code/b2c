package com.mans.ecommerce.b2c.service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.mans.ecommerce.b2c.controller.utills.dto.ProductDto;
import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.product.Product;
import com.mans.ecommerce.b2c.domain.entity.product.subEntity.BasicInfo;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import com.mans.ecommerce.b2c.domain.exception.ResourceNotFoundException;
import com.mans.ecommerce.b2c.repository.product.ProductRepository;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class ProductService
{

    private final int FIVE_MINUTES = 300000;

    @Getter
    private final String NOT_FOUNT_TEMPLATE = "Couldn't find product with sku = %s";

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository)
    {
        this.productRepository = productRepository;
    }

    public Product findById(String id)
    {
        Optional<Product> optionalDetails = productRepository.findById(id);

        if (!optionalDetails.isPresent())
        {
            throw new ResourceNotFoundException(String.format(NOT_FOUNT_TEMPLATE, id));
        }
        return optionalDetails.get();
    }

    public ProductInfo getProductInfo(ProductDto dto)
    {
        String mainSku = getMainSku();
        Product product = productRepository.getProductToAddToCart(mainSku);
        return mapProductToProductInfo(product, dto);
    }

    public void unlock(Cart cart, ProductInfo productInfo)
    {
        if (!cart.isActive() && expireIn5MinsOrLess(cart.getExpireDate()))
        {
            return;
        }

        if (!productInfo.isLocked())
        {
            return;
        }
        String sku = productInfo.getSku();
        int quantityToUnlock = productInfo.getQuantity();
        productRepository.unlock(sku, quantityToUnlock);
    }

    public void unlock(Cart cart, List<ProductInfo> productInfoList)
    {
        for (ProductInfo productInfo : productInfoList)
        {
            unlock(cart, productInfo);
        }
    }

    private String getMainSku()
    {
    }

    private int getLockedQuantity(int availableQuantity, int requestedQuantity)
    {
        if (availableQuantity > requestedQuantity)
        {
            return requestedQuantity;
        }
        return availableQuantity;
    }

    private int lock(ProductInfo cartProduct)
    {
        String sku = cartProduct.getSku();
        int requestedQuantity = cartProduct.getQuantity();
        int availableQuantity = productRepository.lock(sku, requestedQuantity);

        if (availableQuantity < requestedQuantity)
        {
            return availableQuantity;
        }

        return requestedQuantity;
    }

    private boolean expireIn5MinsOrLess(Date expireDate)
    {
        long initTime = expireDate.getTime();
        long now = Instant.now().toEpochMilli();
        return now - initTime <= FIVE_MINUTES;
    }

    private ProductInfo mapProductToProductInfo(Product product, ;)
    {
        BasicInfo productBasicInfo = product.getBasicInfo();
        int availableQuantity = product.getAvailability().getNumUnitsAvailable();

        return ProductInfo
                       .builder()
                       .sku(product.getSku())
                       .title(productBasicInfo.getTitle())
                       .imageUrl(productBasicInfo.getMainImageUrl())
                       .price(productBasicInfo.getPriceGlimpse())
                       .quantity(quantity)
                       .locked(true)
                       .build();
    }
}
