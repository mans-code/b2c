package com.mans.ecommerce.b2c.domain.entity.product.subEntity;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = {})
public class AvailableVariation
{
    private Map<String, String> variation; // key:value  keySets will correspond to whole  keySets defined at ProductVariation variation

    private double price;

    private int quantity;

    private String description;

    private List<String> imagesUrl;
}
