package com.mans.ecommerce.b2c.domain.entity.sharedSubEntity;

import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {})
public class ProductInfo
{
    private String productId;

    private String productName;

    private String imageUrl;

    private Integer quantity;

    private Double price;

    private Map<String, String> variation;
}
