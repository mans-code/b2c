package com.mans.ecommerce.b2c.domain.entity.customer;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = {})
public class CustomerOrder
{
    private String id;

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

    protected CustomerOrder()
    {

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
