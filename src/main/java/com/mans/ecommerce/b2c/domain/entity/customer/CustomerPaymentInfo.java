package com.mans.ecommerce.b2c.domain.entity.customer;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString(exclude = {})
@Document(collection = "customer_payment_detail")
public class CustomerPaymentInfo
{
    @Id
    private String id;

    private List<CreditCard> creditCardList;

    protected CustomerPaymentInfo()
    {

    }

    public CustomerPaymentInfo(
            String id,
            List<CreditCard> creditCardList)
    {
        this.id = id;
        this.creditCardList = creditCardList;
    }

    @Getter
    @Setter
    @ToString(exclude = {})
    public static class CreditCard
    {
        private String nameOnCard;

        private String cardNumber; // only the last four digits TODO

        private String creditCardType; //TODO enum

        private String currency; //TODO enum

        private int expirationMonth;

        private int expirationYear;

        private boolean primary;

        private Address billingAddress;

        public CreditCard(
                String nameOnCard,
                String cardNumber,
                String creditCardType,
                String currency,
                int expirationMonth,
                int expirationYear,
                boolean primary,
                Address billingAddress)
        {
            this.nameOnCard = nameOnCard;
            this.cardNumber = cardNumber;
            this.creditCardType = creditCardType;
            this.currency = currency;
            this.expirationMonth = expirationMonth;
            this.expirationYear = expirationYear;
            this.primary = primary;
            this.billingAddress = billingAddress;
        }
    }

}
