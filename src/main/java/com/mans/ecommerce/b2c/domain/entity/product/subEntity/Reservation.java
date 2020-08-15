package com.mans.ecommerce.b2c.domain.entity.product.subEntity;

import java.util.Date;

import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import lombok.*;
import org.bson.types.ObjectId;
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

    private ObjectId cartId;

    private int quantity;

    @CreatedDate
    private Date createdOn;

    public Reservation(ProductInfo productInfo, ObjectId cartId, Integer quantity)
    {
        this.sku = productInfo.getSku();
        this.variationId = productInfo.getVariationId();
        this.cartId = cartId;
        this.quantity = quantity;
    }
}
