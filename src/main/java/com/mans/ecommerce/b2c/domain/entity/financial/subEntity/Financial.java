package com.mans.ecommerce.b2c.domain.entity.financial.subEntity;

import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = {})
@AllArgsConstructor
public class Financial
{
    private Money salesTax;

    private Money shippingCost;

    private Money discount;

    private Money total;

    public Financial(Money total)
    {
        this.total = total;
    }
}
