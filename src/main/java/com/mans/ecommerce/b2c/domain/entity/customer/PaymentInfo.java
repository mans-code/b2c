package com.mans.ecommerce.b2c.domain.entity.customer;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.Address;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString(exclude = {})
@Document(collection = "customer_payment_detail")
public class PaymentInfo
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
}
