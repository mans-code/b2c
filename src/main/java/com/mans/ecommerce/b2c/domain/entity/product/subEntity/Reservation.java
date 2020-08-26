package com.mans.ecommerce.b2c.domain.entity.product.subEntity;

import java.util.Date;

import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class Reservation
{
    private String sku;

    private String variationId;

    private String id;

    private int quantity;

    @CreatedDate
    private Date createdOn;

    public Reservation(ProductInfo productInfo, String cartId, Integer quantity)
    {
        this.sku = productInfo.getSku();
        this.variationId = productInfo.getVariationId();
        this.id = cartId;
        this.quantity = quantity;
    }
}
