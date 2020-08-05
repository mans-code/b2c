package com.mans.ecommerce.b2c.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.mans.ecommerce.b2c.controller.utills.dto.ProductDto;
import com.mans.ecommerce.b2c.domain.entity.product.Product;
import com.mans.ecommerce.b2c.domain.entity.product.QAndA;
import com.mans.ecommerce.b2c.domain.entity.product.Review;
import com.mans.ecommerce.b2c.domain.entity.product.subEntity.Availability;
import com.mans.ecommerce.b2c.domain.entity.product.subEntity.BasicInfo;
import com.mans.ecommerce.b2c.domain.entity.product.subEntity.Reservation;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import com.mans.ecommerce.b2c.domain.exception.ResourceNotFoundException;
import com.mans.ecommerce.b2c.repository.product.ProductRepository;
import com.mans.ecommerce.b2c.repository.product.QAndARepository;
import com.mans.ecommerce.b2c.repository.product.ReviewRepository;
import com.mans.ecommerce.b2c.utill.response.QAndAPage;
import com.mans.ecommerce.b2c.utill.response.ReviewPage;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductService
{

    private final int PAGE_SIZE = 7;

    @Getter
    private final String NOT_FOUNT_TEMPLATE = "Couldn't find product with sku = %s";

    @Getter
    private final String REVIEW_LINK_TEMPLATE = "/products/%s?page=%d&sortby=%s";

    private ProductRepository productRepository;

    private QAndARepository qAndARepository;

    private ReviewRepository reviewRepository;

    public ProductService(
            ProductRepository productRepository,
            QAndARepository qAndARepository,
            ReviewRepository reviewRepository)
    {
        this.productRepository = productRepository;
        this.qAndARepository = qAndARepository;
        this.reviewRepository = reviewRepository;
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

    public boolean unlock(String sku, String variationId, String cartId, int quantityToUnlock)
    {
        return productRepository.unlock(sku, variationId, cartId, quantityToUnlock);
    }

    public boolean partialUnlock(
            String sku,
            String variationId,
            String cartId,
            int quantityToUnlock,
            int newReservedQuantity)
    {
        return productRepository.partialUnlock(sku, variationId, cartId, quantityToUnlock, newReservedQuantity);
    }

    public boolean addReservation(Reservation reservation)
    {
        return productRepository.addReservation(reservation);
    }

    public boolean updateReservation(Reservation reservation, int locked)
    {
        return productRepository.updateReservation(reservation, locked);
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
