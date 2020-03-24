package com.mans.ecommerce.b2c.domain.entity;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.subEntity.Address;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class CustomerPaymentInfo
{
    @Id
    private String id;

    private List<CreditCard> creditCardList;

    private List<Address> billingAddress;

    private class CreditCard
    {
        private String nameOnCard;

        private long cardNumber; // only the last four digits TODO

        private String currency; //TODO enum

        private int expirationMonth;

        private int expirationYear;

        private boolean primary;
    }

    public List<CreditCard> getCreditCardList()
    {
        return creditCardList;
    }

    public List<Address> getBillingAddress()
    {
        return billingAddress;
    }

    public void setCreditCardList(List<CreditCard> creditCardList)
    {
        this.creditCardList = creditCardList;
    }

    public void setBillingAddress(List<Address> billingAddress)
    {
        this.billingAddress = billingAddress;
    }
}
