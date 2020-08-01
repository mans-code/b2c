package com.mans.ecommerce.b2c.domain.entity.product.subEntity;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

@NoArgsConstructor
@Setter
@Getter
@ToString
public class Reservation
{
    private String id;

    private int quantity;

    private String variationId;

    @CreatedDate
    private Date createdOn;

    public Reservation(String id, String variationId, int quantity)
    {
        this.id = id;
        this.variationId = variationId;
        this.quantity = quantity;
    }
}
