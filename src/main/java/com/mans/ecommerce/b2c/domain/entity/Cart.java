package com.mans.ecommerce.b2c.domain.entity;

import java.util.Date;
import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.subEntity.ProductInfo;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Cart
{
    @Id
    private String id;

    private String customerId;

    private boolean active;

    private Date expireDate;

    private List<ProductInfo> productInfo;

    private double totalPrice;

    private int totalQuantity;

    protected Cart()
    {

    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getCustomerId()
    {
        return customerId;
    }

    public boolean isActive()
    {
        return active;
    }

    public Date getExpireDate()
    {
        return expireDate;
    }

    public List<ProductInfo> getProductInfo()
    {
        return productInfo;
    }

    public double getTotalPrice()
    {
        return totalPrice;
    }

    public int getTotalQuantity()
    {
        return totalQuantity;
    }

    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public void setExpireDate(Date expireDate)
    {
        this.expireDate = expireDate;
    }

    public void setProductInfo(List<ProductInfo> productInfo)
    {
        this.productInfo = productInfo;
    }

    public void setTotalPrice(double totalPrice)
    {
        this.totalPrice = totalPrice;
    }

    public void setTotalQuantity(int totalQuantity)
    {
        this.totalQuantity = totalQuantity;
    }
}
