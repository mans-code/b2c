package com.mans.ecommerce.b2c.domain.entity.sharedSubEntity;

import com.mans.ecommerce.b2c.domain.enums.PricingStrategy;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {})
public class Price
{

    private PricingStrategy pricingStrategy;

    private Money money;

    public Price(Money money)
    {
        this.money = money;
        this.pricingStrategy = PricingStrategy.STATIC;
    }

    public Price(Money money, PricingStrategy pricingStrategy)
    {
        this.money = money;
        this.pricingStrategy = pricingStrategy;
    }

    public Price(Price price)
    {
        money = new Money(price.money);
        this.pricingStrategy = price.pricingStrategy;
    }
}
