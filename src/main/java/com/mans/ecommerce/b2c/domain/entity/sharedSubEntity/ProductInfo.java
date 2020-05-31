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

    private String title;

    private String imageUrl;

    private int quantity;

    private Price price;

    private Map<String, String> variation;

    private boolean locked;
}
