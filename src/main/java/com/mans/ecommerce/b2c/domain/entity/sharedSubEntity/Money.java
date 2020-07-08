package com.mans.ecommerce.b2c.domain.entity.sharedSubEntity;

import java.math.BigDecimal;

import com.mans.ecommerce.b2c.domain.enums.Currency;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(exclude = {})
public class Money
{
    private BigDecimal amount;

    private Currency currency;

    public Money(BigDecimal amount, Currency currency)
    {
        this.amount = amount;
        this.currency = currency;
    }
}
