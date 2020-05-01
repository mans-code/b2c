package com.mans.ecommerce.b2c.domain.entity.customer;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.customer.subEntity.CreditCard;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = {})
public class CustomerPaymentInfo
{
    private String id;

    private List<CreditCard> creditCardList;

    protected CustomerPaymentInfo()
    {

    }
}
