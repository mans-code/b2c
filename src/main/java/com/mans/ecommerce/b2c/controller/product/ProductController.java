package com.mans.ecommerce.b2c.controller.product;

import javax.validation.constraints.NotBlank;

import com.mans.ecommerce.b2c.domain.entity.product.Product;
import com.mans.ecommerce.b2c.domain.enums.SortBy;
import com.mans.ecommerce.b2c.service.ProductService;
import com.mans.ecommerce.b2c.utill.response.QAndAPage;
import com.mans.ecommerce.b2c.utill.response.ReviewPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products/{sku}")
public class ProductController
{
    @Autowired
    private ProductService productService;

    @GetMapping("/detail")
    public Product getProduct(@PathVariable("sku") @NotBlank String sku)
    {
        return productService.getProductDetails(sku);
    }

    @GetMapping("/qanda")
    public QAndAPage getQ8A(
            @PathVariable("sku") @NotBlank String sku,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "helpful") SortBy sortBy)
    {
        return productService.getQ8A(sku, page, sortBy.getSortBy());
    }

    @GetMapping("/review")
    public ReviewPage getReview(
            @PathVariable("sku") @NotBlank String sku,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(value = "sortby", defaultValue = "helpful") SortBy sortBy)
    {
        return productService.getReview(sku, page, sortBy.getSortBy());
    }

}
