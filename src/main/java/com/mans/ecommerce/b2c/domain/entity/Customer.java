package com.mans.ecommerce.b2c.domain.entity;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.subEntity.Address;
import com.mans.ecommerce.b2c.domain.entity.subEntity.ProductInfo;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Customer
{
    @Id
    private String id;
    private String username;
    private String password;
    private String name;
    private String phoneNumber;
    private List<Address> shippingAddresses;
    private List<Address> billingAddresses;
    private List<ProductInfo> recommendation;

    protected Customer()
    {

    }

    public String getId()
    {
        return id;
    }

    public List<ProductInfo> getRecommendation()
    {
        return recommendation;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setRecommendation(List<ProductInfo> recommendation)
    {
        this.recommendation = recommendation;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public void setShippingAddresses(List<Address> shippingAddresses)
    {
        this.shippingAddresses = shippingAddresses;
    }

    public void setBillingAddresses(List<Address> billingAddresses)
    {
        this.billingAddresses = billingAddresses;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public String getName()
    {
        return name;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public List<Address> getShippingAddresses()
    {
        return shippingAddresses;
    }

    public List<Address> getBillingAddresses()
    {
        return billingAddresses;
    }
}
