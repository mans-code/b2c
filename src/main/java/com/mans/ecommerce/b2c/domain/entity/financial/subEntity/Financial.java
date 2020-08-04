package com.mans.ecommerce.b2c.domain.entity.financial.subEntity;

import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.Money;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = {})
public class Financial
{
    private Money salesTax;

    private Money shippingCost;

    private Money discount;

    private Money total;
}
