package com.mans.ecommerce.b2c.domain.entity.sharedSubEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = {})
public class Price
{

    private String type;

    private double price;

    private String currency;

    public Price(double price, String currency)
    {
        this.price = price;
        this.currency = currency;
        this.type = "STATIC";
    }

    public Price()
    {
        this.price = 0.00;
        this.currency = "NULL";
        this.type = "NULL";
    }
}
