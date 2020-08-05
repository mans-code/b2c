package com.mans.ecommerce.b2c.domain.entity.sharedSubEntity;

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

    private String variationId;

    private String title;

    private String imageUrl;

    private int quantity;

    private Price price;

    public ProductInfo(ProductInfo clone)
    {
        sku = clone.sku;
        variationId = clone.variationId;
        title = clone.title;
        imageUrl = clone.imageUrl;
        quantity = clone.quantity;
        price = new Price(clone.price);
    }
}
