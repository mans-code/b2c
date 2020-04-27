package com.mans.ecommerce.b2c.domain.entity.sharedSubEntity;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductInfo
{
    private String productDetailId;

    private String productName;

    private String imageUrl;

    private int quantity;

    private double price;

    private Map<String, String> variation;

    protected ProductInfo()
    {
    }

}
