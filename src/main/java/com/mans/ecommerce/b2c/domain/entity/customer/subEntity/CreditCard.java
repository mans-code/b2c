package com.mans.ecommerce.b2c.domain.entity.customer.subEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = {"cardNumber"})
public class CreditCard
{
    protected CreditCard()
    {
    }

    private String nameOnCard;

    private String cardNumber; // only the last four digits TODO

    private String creditCardType; //TODO enum

    private String currency; //TODO enum

    private int expirationMonth;

    private int expirationYear;

    private boolean primary;

    private Address billingAddress;

}
