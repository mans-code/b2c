package com.mans.ecommerce.b2c.controller.product;

import javax.validation.constraints.NotBlank;

import com.mans.ecommerce.b2c.domain.entity.product.Product;
import com.mans.ecommerce.b2c.domain.enums.SortBy;
import com.mans.ecommerce.b2c.server.eventListener.entity.ClickedOnProductEvent;
import com.mans.ecommerce.b2c.service.ProductService;
import com.mans.ecommerce.b2c.utill.response.Page;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products/{sku}")
public class ProductController
{

    private ProductService productService;

    private ApplicationEventPublisher publisher;

    ProductController(ProductService productService, ApplicationEventPublisher publisher)
    {
        this.productService = productService;
        this.publisher = publisher;
    }

    @GetMapping("/detail")
    public Mono<Product> getProduct(@PathVariable("sku") @NotBlank String sku, ServerHttpRequest req)
    {
        publisher.publishEvent(new ClickedOnProductEvent(sku, req));
        return productService.getProductDetails(sku);
    }

    @GetMapping("/qanda")
    public Mono<Page> getQ8A(
            @PathVariable("sku") @NotBlank String sku,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "helpful") SortBy sortBy)
    {
        return productService.getQ8A(sku, page, sortBy.getSortBy());
    }

    @GetMapping("/review")
    public Mono<Page> getReview(
            @PathVariable("sku") @NotBlank String sku,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(value = "sortby", defaultValue = "helpful") SortBy sortBy)
    {
        return productService.getReview(sku, page, sortBy.getSortBy());
    }

}
