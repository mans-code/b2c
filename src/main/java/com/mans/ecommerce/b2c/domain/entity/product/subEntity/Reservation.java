package com.mans.ecommerce.b2c.domain.entity.product.subEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Setter
@Getter
@ToString
public class Reservation
{
    private String id;

    private int quantity;
}
