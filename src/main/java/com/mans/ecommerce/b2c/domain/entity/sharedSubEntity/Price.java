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

    private double amount;

    private String currency;

    public Price(double amount, String currency)
    {
        this.amount = amount;
        this.currency = currency;
        this.type = "STATIC";
    }

    public Price()
    {
        this.amount = 0.00;
        this.currency = "CAD";
        this.type = "NULL";
    }
}
