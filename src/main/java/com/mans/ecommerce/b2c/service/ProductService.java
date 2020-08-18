package com.mans.ecommerce.b2c.service;

import java.util.List;

import com.mans.ecommerce.b2c.controller.utills.dto.ProductDto;
import com.mans.ecommerce.b2c.domain.entity.product.Product;
import com.mans.ecommerce.b2c.domain.entity.product.QAndA;
import com.mans.ecommerce.b2c.domain.entity.product.Review;
import com.mans.ecommerce.b2c.domain.entity.product.subEntity.Availability;
import com.mans.ecommerce.b2c.domain.entity.product.subEntity.BasicInfo;
import com.mans.ecommerce.b2c.domain.entity.product.subEntity.Variation;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import com.mans.ecommerce.b2c.domain.exception.ResourceNotFoundException;
import com.mans.ecommerce.b2c.repository.product.ProductRepository;
import com.mans.ecommerce.b2c.repository.product.QAndARepository;
import com.mans.ecommerce.b2c.repository.product.ReviewRepository;
import com.mans.ecommerce.b2c.utill.response.Page;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.server.reactive.ServerHttpRequest;
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

    private FeedService feedService;

    public ProductService(
            ProductRepository productRepository,
            QAndARepository qAndARepository,
            FeedService feedService,
            ReviewRepository reviewRepository)
    {
        this.productRepository = productRepository;
        this.qAndARepository = qAndARepository;
        this.reviewRepository = reviewRepository;
        this.feedService = feedService;
    }

    public Mono<Product> getProductDetails(String sku, ServerHttpRequest req)
    {
        Mono<Product> productMono = productRepository.getBySku(sku);
        productMono.doOnSuccess(product -> throwIfNull(product, sku));
        productMono.doOnSuccess(product -> feedService.addToClicked(sku, req));
        return productMono.flatMap(product -> hideQuantity(product));
    }

    public Mono<ProductInfo> getProductInfo(ProductDto dto)
    {
        String sku = dto.getSku();
        String variationId = getVariationId(dto);
        Mono<Variation> variationMono = productRepository.getVariation(sku, variationId);
        variationMono.doOnSuccess(variation -> throwIfNull(variation, sku));
        return variationMono.flatMap(variation -> mapProductToProductInfo(variation, dto));
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
                .getVariationsDetails()
                .values()
                .forEach(variation -> {
                    Availability availability = variation.getAvailability();
                    if (availability.getQuantity() > 0)
                    {
                        availability.setQuantity(1);
                    }
                });

        return Mono.just(product);
    }

    private Pageable getPageable(Integer page, String sortBy)
    {
        Sort sort = Sort.by(sortBy).descending();
        return PageRequest.of(page, PAGE_SIZE, sort);
    }

    private Mono<ProductInfo> mapProductToProductInfo(Variation variation, ProductDto dto)
    {
        BasicInfo basicInfo = variation.getBasicInfo();
        int quantity = getQuantity(variation, dto);
        String variationId = getVariationId(dto);

        ProductInfo productInfo = ProductInfo
                                          .builder()
                                          .sku(dto.getSku())
                                          .variationId(variationId)
                                          .title(basicInfo.getTitle())
                                          .imageUrl(basicInfo.getImageUrl())
                                          .money(basicInfo.getMoney())
                                          .quantity(quantity)
                                          .build();

        return Mono.just(productInfo);
    }

    private void throwIfNull(Object object, String sku)
    {
        if (object == null)
        {
            throw new ResourceNotFoundException(String.format(NOT_FOUNT_TEMPLATE, sku));
        }
    }

    private int getQuantity(Variation variation, ProductDto dto)
    {

        int requestedQuantity = dto.getQuantity();
        int availableQuantity = variation.getAvailability()
                                         .getQuantity();

        if (requestedQuantity > availableQuantity)
        {
            return availableQuantity;
        }
        return requestedQuantity;
    }

    private String getVariationId(ProductDto dto)
    {
        return dto.getVariationId() == null ? dto.getSku() : dto.getVariationId();
    }
}
