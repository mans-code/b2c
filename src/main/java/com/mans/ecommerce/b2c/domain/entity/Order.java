package com.mans.ecommerce.b2c.domain.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
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

    public String getId()
    {
        return id;
    }

    public String getCustomerId()
    {
        return customerId;
    }

    public Date getCreatedOn()
    {
        return createdOn;
    }

    public Date getUpdatedOn()
    {
        return updatedOn;
    }

    public double getSalesTax()
    {
        return salesTax;
    }

    public double getShippingCost()
    {
        return shippingCost;
    }

    public double getTotalPrice()
    {
        return totalPrice;
    }

    public double getDiscount()
    {
        return discount;
    }

    public double getImportFee()
    {
        return importFee;
    }

    public OrderDetails getDetails()
    {
        return details;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }

    public void setCreatedOn(Date createdOn)
    {
        this.createdOn = createdOn;
    }

    public void setUpdatedOn(Date updatedOn)
    {
        this.updatedOn = updatedOn;
    }

    public void setSalesTax(double salesTax)
    {
        this.salesTax = salesTax;
    }

    public void setShippingCost(double shippingCost)
    {
        this.shippingCost = shippingCost;
    }

    public void setTotalPrice(double totalPrice)
    {
        this.totalPrice = totalPrice;
    }

    public void setDiscount(double discount)
    {
        this.discount = discount;
    }

    public void setImportFee(double importFee)
    {
        this.importFee = importFee;
    }

    public void setDetails(OrderDetails details)
    {
        this.details = details;
    }
}
