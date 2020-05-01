package com.mans.ecommerce.b2c.domain.entity.sharedSubEntity;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = {})
public class ProductInfo
{
    private String productId;

    private String productName;

    private String imageUrl;

    private int quantity;

    private double price;

    private Map<String, String> variation;
}
