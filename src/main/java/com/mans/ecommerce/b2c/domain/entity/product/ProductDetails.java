package com.mans.ecommerce.b2c.domain.entity.product;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.product.subEntity.ProductVariation;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.Price;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = {})
public class ProductDetails
{

    // About

    private String id;

    private String by;

    private String shortDescription; // HTML

    private String productInformation; //  HTML

    private String fromManufacturer; // HTML

    private String description; // HTML

    private Price price;

    //feedback

    private int numOfQuestions;

    private List<ProductQAndA> topProductQAndA;

    private int numReviews;

    private List<ProductReview> topProductReviews;

    private ProductVariation productVariations;

    // others products
    private List<ProductInfo> similarItems;
}
