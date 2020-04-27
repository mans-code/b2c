package com.mans.ecommerce.b2c.domain.entity.customer;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString(exclude = {})
@Document(collection = "order")
public class Order
{
    @Id
    private String id;

    private String customerId;

    //Dates
    private Date createdOn;

    private Date updatedOn;

    // money
    private double salesTax;

    private double shippingCost;

    private double totalPrice;

    private double discount;

    private double importFee;

    //Details
    private OrderDetails details;

    private class OrderDetails
    {
        private OrderHistory OrderHistory;

        private OrderShippingInfo shippingInfo;

        private OrderPaymentInfo payment;

        private class OrderHistory
        {
            private String status; //TODO enum

            private String note;

            private Date cratedOn;

            private Date updateOn;
        }

        private class OrderShippingInfo
        {
            private Date cratedOn;

            private Date updateOn;

            private String status; // TODO enum

            private String method; //TODO enum

            private double charge;

            private String trackingNumber;
        }

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
