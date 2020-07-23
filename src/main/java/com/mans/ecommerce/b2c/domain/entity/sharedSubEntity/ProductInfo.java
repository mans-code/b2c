package com.mans.ecommerce.b2c.domain.entity.sharedSubEntity;

import java.util.Map;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {})
public class ProductInfo
{
    private String sku;

    private String title;

    private String imageUrl;

    private int quantity;

    private Price price;

    private Map<String, String> variation;

    private boolean locked;
}
