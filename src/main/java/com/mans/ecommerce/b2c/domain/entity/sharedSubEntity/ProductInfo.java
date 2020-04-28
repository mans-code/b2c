package com.mans.ecommerce.b2c.domain.entity.sharedSubEntity;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductInfo
{
    private String productId;

    private String productName;

    private String imageUrl;

    private int quantity;

    private double price;

    private Map<String, String> variation;

    protected ProductInfo()
    {
    }

    public ProductInfo(
            String productId,
            String productName,
            String imageUrl,
            int quantity,
            double price,
            Map<String, String> variation)
    {
        this.productId = productId;
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.price = price;
        this.variation = variation;
    }
}
