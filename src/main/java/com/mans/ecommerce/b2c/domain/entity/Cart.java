package com.mans.ecommerce.b2c.domain.entity;

import java.util.Date;
import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.subEntity.ProductInfo;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Cart
{

    private String customerId;
    private boolean active;
    private Date expireDate;
    private List<ProductInfo> productInfo;
    private double totalPrice;
    private int totalQuantity;



}
