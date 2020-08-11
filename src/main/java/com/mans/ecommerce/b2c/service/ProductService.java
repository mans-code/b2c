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
import com.mans.ecommerce.b2c.utill.response.Page;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    public Mono<Product> getProductDetails(String sku)
    {
        Mono<Product> productMono = productRepository.getBySku(sku);
        productMono.doOnSuccess(product -> throwIfNull(product));
        return productMono.flatMap(product -> hideQuantity(product));
    }

    public Mono<ProductInfo> getProductInfo(ProductDto dto)
    {
        String sku = dto.getSku();
        Mono<Product> productMono = productRepository.getProductToAddToCart(sku);
        productMono.doOnSuccess(product -> throwIfNull(product));
        return productMono.flatMap(product -> mapProductToProductInfo(product, dto));
    }

    public Mono<Page> getQ8A(String sku, int page, String sortBy)
    {
        Pageable pageable = getPageable(page, sortBy);
        Flux<QAndA> qAndAFlux = qAndARepository.findBySku(sku, pageable);
        Mono<List<QAndA>> qAndAMono = qAndAFlux.collectList();
        Mono<Page> qAndAPageMono = qAndAMono.flatMap(q8AS -> getPage(q8AS, sku, page, sortBy));
        return qAndAPageMono;
    }

    public Mono<Page> getReview(String sku, int page, String sortBy)
    {
        Pageable pageable = getPageable(page, sortBy);
        Flux<Review> reviewFlux = reviewRepository.findBySku(sku, pageable);
        Mono<List<Review>> reviewsMono = reviewFlux.collectList();
        Mono<Page> reviewsPageMono = reviewsMono.flatMap(q8AS -> getPage(q8AS, sku, page, sortBy));
        return reviewsPageMono;
    }

    public Mono<Boolean> unlock(String sku, String variationId, ObjectId cartId, int quantityToUnlock)
    {
        return productRepository.unlock(sku, variationId, cartId, quantityToUnlock);
    }

    public Mono<Boolean> partialUnlock(
            String sku,
            String variationId,
            ObjectId cartId,
            int quantityToUnlock,
            int newReservedQuantity)
    {
        return productRepository.partialUnlock(sku, variationId, cartId, quantityToUnlock, newReservedQuantity);
    }

    public Mono<Boolean> addReservation(Reservation reservation)
    {
        return productRepository.addReservation(reservation);
    }

    public Mono<Boolean> updateReservation(Reservation reservation, int locked)
    {
        return productRepository.updateReservation(reservation, locked);
    }

    private <T> Mono<Page> getPage(List<T> q8AS, String sku, int pageNum, String sortBy)
    {
        String next = getNext(q8AS, sku, pageNum, sortBy);
        Page page = new Page(q8AS, next);
        return Mono.just(page);
    }

    private <T> String getNext(List<T> list, String sku, int page, String sortBy)
    {
        String next = null;
        if (list.isEmpty())
        {
            next = String.format(REVIEW_LINK_TEMPLATE, sku, ++page, sortBy);
        }
        return next;
    }

    private Mono<Product> hideQuantity(Product product)
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

        return Mono.just(product);
    }

    private Pageable getPageable(Integer page, String sortBy)
    {
        Sort sort = Sort.by(sortBy).descending();
        return PageRequest.of(page, PAGE_SIZE, sort);
    }

    private Mono<ProductInfo> mapProductToProductInfo(Product product, ProductDto dto)
    {
        BasicInfo productBasicInfo = product.getBasicInfo();
        int quantity = getQuantity(product, dto);
        String variationId = getVariationId(product, dto);

        ProductInfo productInfo = ProductInfo
                                          .builder()
                                          .sku(dto.getSku())
                                          .variationId(variationId)
                                          .title(productBasicInfo.getTitle())
                                          .imageUrl(productBasicInfo.getMainImageUrl())
                                          .price(productBasicInfo.getPriceGlimpse())
                                          .quantity(quantity)
                                          .build();

        return Mono.just(productInfo);
    }

    private void throwIfNull(Product product)
    {
        if (product == null)
        {
            throw new ResourceNotFoundException(String.format(NOT_FOUNT_TEMPLATE, product.getSku()));
        }
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
