package com.mans.ecommerce.b2c.domain.entity.sharedSubEntity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {})
public class ProductInfo implements Cloneable
{
    private String sku;

    private String variationId;

    private String title;

    private String imageUrl;

    private int quantity;

    private Price price;

    @Override public Object clone()
    {
        return ProductInfo
                       .builder()
                       .sku(sku)
                       .variationId(variationId)
                       .title(title)
                       .imageUrl(imageUrl)
                       .quantity(quantity)
                       .price(price);
    }
}
