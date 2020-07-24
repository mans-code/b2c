package com.mans.ecommerce.b2c.domain.entity.product.subEntity;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class VariationLevel
{
    private String title;

    private int priority;//FrontEnd ordering

    List<Node> nodes;
}
