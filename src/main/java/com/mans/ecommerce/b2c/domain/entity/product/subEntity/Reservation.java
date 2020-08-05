package com.mans.ecommerce.b2c.domain.entity.product.subEntity;

import java.util.Date;

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

    private String cartId;

    private int quantity;

    @CreatedDate
    private Date createdOn;

    public Reservation(String sku, String variationId, String cartId, int quantity)
    {
        this.sku = sku;
        this.variationId = variationId;
        this.cartId = cartId;
        this.quantity = quantity;
        createdOn = new Date();
    }

}
