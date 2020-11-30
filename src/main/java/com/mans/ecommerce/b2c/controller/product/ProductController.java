package com.mans.ecommerce.b2c.controller.product;

import javax.validation.constraints.NotBlank;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.product.Product;
import com.mans.ecommerce.b2c.domain.enums.SortBy;
import com.mans.ecommerce.b2c.service.ProductService;
import com.mans.ecommerce.b2c.utill.response.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products/{sku}")
@Tag(name = "Product api", description = "get all info about products")
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
    @Operation(description = "returns the product full details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "product with the given id not found")
    })
    public Mono<Product> getProduct(@PathVariable("sku") @NotBlank String sku, ServerHttpRequest req)
    {
        return productService.getProductDetails(sku, req);
    }

    @GetMapping("/qanda")
    @Operation(description = "returns the product Q And A")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "product with the given id not found")
    })
    public Mono<Page> getQ8A(
            @PathVariable("sku") @NotBlank String sku,

            @Parameter( name =  "page",
                    description = "the requested page number",
                    example = "1",
                    required = false)
            @RequestParam(defaultValue = "0") Integer page,

            @Parameter( name =  "sortBy",
                    description = "QandA sort by attribute",
                    example = "helpful",
                    required = false)
            @RequestParam(defaultValue = "helpful") SortBy sortBy)
    {
        return productService.getQ8A(sku, page, sortBy.getSortBy());
    }

    @GetMapping("/review")
    @Operation(description = "returns the product reviews")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "product with the given id not found")
    })
    public Mono<Page> getReview(
            @PathVariable("sku") @NotBlank String sku,

            @Parameter( name =  "page",
                    description = "the requested page number",
                    example = "1",
                    required = false)
            @RequestParam(defaultValue = "0") Integer page,

            @Parameter( name =  "sortBy",
                    description = "QandA sort by attribute",
                    example = "helpful",
                    required = false)
            @RequestParam(value = "sortby", defaultValue = "helpful") SortBy sortBy)
    {
        return productService.getReview(sku, page, sortBy.getSortBy());
    }

}
