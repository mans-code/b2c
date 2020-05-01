package com.mans.ecommerce.b2c.domain.entity.product.subEntity;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = {})
public class ProductVariation
{
    private Map<String, List<String>> variation; // key:values
    private List<AvailableVariation> availableVariation;
}
