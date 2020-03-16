package com.mans.ecommerce.b2c.domain.entity;

import java.util.Date;

import com.mans.ecommerce.b2c.domain.entity.subEntity.OrderDetails;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Order
{

    private String customerId;
    private Date createdOn;
    private Date updatedon;
    private OrderDetails details;
    private double salesTax;
    private double shippingCost;
    private double totalPrice;
    private double discount;

    private class OrderDetails
    {

    }
}
