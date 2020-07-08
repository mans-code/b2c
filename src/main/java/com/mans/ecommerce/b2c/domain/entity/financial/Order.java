package com.mans.ecommerce.b2c.domain.entity.financial;

import java.util.Date;

import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.Price;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {})
public class Order
{
    @Id
    private String id;

    private String CustomerId;

    @CreatedDate
    private String createdOn;

    @LastModifiedDate
    private Date updatedOn;

    private Price totalPrice;

    private Financial CC;


    //Details
    private OrderDetails details;


    @Getter
    @Setter
    @ToString(exclude = {})
    private class Financial
    {
        // money
        private double salesTax;

        private double shippingCost;

        private double discount;
    }

    @Getter
    @Setter
    @ToString(exclude = {})
    private class OrderDetails
    {
        private OrderHistory OrderHistory;

        private OrderShippingInfo shippingInfo;

        private OrderPaymentInfo payment;

        @Getter
        @Setter
        @ToString(exclude = {})
        private class OrderHistory
        {
            private String status; //TODO enum

            private String desc;

            private String note;

            private Date cratedOn;

            private Date updateOn;
        }

        @Getter
        @Setter
        @ToString(exclude = {})
        private class OrderShippingInfo
        {
            private Date cratedOn;

            private Date updateOn;

            private String status; // TODO enum

            private String method; //TODO enum

            private double charge;

            private String trackingNumber;
        }

        @Getter
        @Setter
        @ToString(exclude = {})
        private class OrderPaymentInfo
        {
            private String cardOwnerName;

            private String paymentNum;

            private String lastFourDigits;

            private double amount;

            private String status; //TODO enum
        }
    }
}
