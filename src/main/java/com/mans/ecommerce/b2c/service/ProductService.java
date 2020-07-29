package com.mans.ecommerce.b2c.service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.mans.ecommerce.b2c.controller.utills.dto.ProductDto;
import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.product.Product;
import com.mans.ecommerce.b2c.domain.entity.product.QAndA;
import com.mans.ecommerce.b2c.domain.entity.product.Review;
import com.mans.ecommerce.b2c.domain.entity.product.subEntity.Availability;
import com.mans.ecommerce.b2c.domain.entity.product.subEntity.BasicInfo;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import com.mans.ecommerce.b2c.domain.exception.ConflictException;
import com.mans.ecommerce.b2c.domain.exception.ResourceNotFoundException;
import com.mans.ecommerce.b2c.repository.product.ProductRepository;
import com.mans.ecommerce.b2c.repository.product.QAndARepository;
import com.mans.ecommerce.b2c.repository.product.ReviewRepository;
import com.mans.ecommerce.b2c.utill.response.QAndAPage;
import com.mans.ecommerce.b2c.utill.response.ReviewPage;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductService
{

    private final int TWO_MINUTES = 120000;

    private final int PAGE_SIZE = 7;

    @Getter
    private final String NOT_FOUNT_TEMPLATE = "Couldn't find product with sku = %s";

    @Getter
    private final String REVIEW_LINK_TEMPLATE = "/products/%s?page=%d&sortby=%s";

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private QAndARepository qAndARepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public ProductService(ProductRepository productRepository)
    {
        this.productRepository = productRepository;
    }

    public Product getProductDetails(String sku)
    {
        Product product = productRepository.getBySku(sku)
                                           .orElseThrow(() -> new ResourceNotFoundException(
                                                   String.format(NOT_FOUNT_TEMPLATE, sku)
                                           ));
        hideQuantity(product);

        return product;
    }

    public ProductInfo getProductInfo(ProductDto dto)
    {
        String sku = dto.getSku();
        Product product = productRepository.getProductToAddToCart(sku)
                                           .orElseThrow(() -> new ResourceNotFoundException(
                                                   String.format(NOT_FOUNT_TEMPLATE, sku)
                                           ));
        return mapProductToProductInfo(product, dto);
    }

    public void unlock(Cart cart, ProductInfo cartProduct)
    {
        if (!cart.isActive() && expireIn2MinsOrLess(cart.getExpireDate()))
        {
            throw new ConflictException();
        }

        String sku = cartProduct.getSku();
        String variationId = cartProduct.getVariationId();
        int quantityToUnlock = cartProduct.getQuantity();
        productRepository.unlock(sku, variationId, quantityToUnlock);
    }

    public void unlock(Cart cart)
    {
        for (ProductInfo productInfo : cart.getProductInfos())
        {
            unlock(cart, productInfo);
        }
    }

    public QAndAPage getQ8A(String sku, int page, String sortBy)
    {
        Pageable pageable = getPageable(page, sortBy);
        List<QAndA> q8AS = qAndARepository.findBySku(sku, pageable);
        String next = String.format(REVIEW_LINK_TEMPLATE, sku, ++page, sortBy);
        return new QAndAPage(q8AS, next);
    }

    public ReviewPage getReview(String sku, int page, String sortBy)
    {
        Pageable pageable = getPageable(page, sortBy);
        List<Review> reviews = reviewRepository.findBySku(sku, pageable);
        String next = String.format(REVIEW_LINK_TEMPLATE, sku, ++page, sortBy);
        return new ReviewPage(reviews, next);
    }

    private void hideQuantity(Product product)
    {
        product
                .getAvailability()
                .values()
                .forEach(variation -> {
                    if (variation.getQuantity() > 0)
                    {
                        variation.setQuantity(1);
                    }
                });
    }

    private Pageable getPageable(Integer page, String sortBy)
    {
        Sort sort = Sort.by(sortBy).descending();
        return PageRequest.of(page, PAGE_SIZE, sort);
    }

    private int lock(ProductInfo cartProduct)
    {
        String sku = cartProduct.getSku();
        String variationId = cartProduct.getVariationId();
        int requestedQuantity = cartProduct.getQuantity();
        int availableQuantity = productRepository.lock(sku, variationId, requestedQuantity);

        if (availableQuantity < requestedQuantity)
        {
            return availableQuantity;
        }

        return requestedQuantity;
    }

    private boolean expireIn2MinsOrLess(Date expireDate)
    {
        long initTime = expireDate.getTime();
        long now = Instant.now().toEpochMilli();
        return now - initTime <= TWO_MINUTES;
    }

    private ProductInfo mapProductToProductInfo(Product product, ProductDto dto)
    {
        BasicInfo productBasicInfo = product.getBasicInfo();
        int quantity = getQuantity(product, dto);
        String variationId = getVariationId(product, dto);

        return ProductInfo
                       .builder()
                       .sku(dto.getSku())
                       .variationId(variationId)
                       .title(productBasicInfo.getTitle())
                       .imageUrl(productBasicInfo.getMainImageUrl())
                       .price(productBasicInfo.getPriceGlimpse())
                       .quantity(quantity)
                       .build();
    }

    private int getQuantity(Product product, ProductDto dto)
    {
        Map<String, Availability> availability = product.getAvailability();
        String variationId = getVariationId(product, dto);
        int requestedQuantity = dto.getQuantity();
        int availableQuantity = availability.get(variationId).getQuantity();

        if (requestedQuantity > availableQuantity)
        {
            return availableQuantity;
        }
        return requestedQuantity;
    }

    private String getVariationId(Product product, ProductDto dto)
    {
        String dtoVariationId = dto.getVariationId();
        return Objects.isNull(dtoVariationId) ?
                       product.getDefaultVariationId() : dtoVariationId;
    }
}
