package com.mans.ecommerce.b2c.domain.entity;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.subEntity.Address;
import com.mans.ecommerce.b2c.domain.entity.subEntity.ProductInfo;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Customer
{
    // validation , verification, and info for backend
    @Id
    private String id;

    private String username;

    private String password;

    private String phoneNumber;

    //links to details info
    private String customerPaymentInfoId; // TODO should this be removed

    private String cartId;

    private String customerFeedId;

    // First Glimose
    private String name;

    private int numOfItemsInCart;

    private List<ProductInfo> recommendation;

    private List<Address> shippingAddresses;

    protected Customer()
    {

    }

    public String getId()
    {
        return id;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public String getCustomerPaymentInfoId()
    {
        return customerPaymentInfoId;
    }

    public String getCartId()
    {
        return cartId;
    }

    public String getCustomerFeedId()
    {
        return customerFeedId;
    }

    public String getName()
    {
        return name;
    }

    public int getNumOfItemsInCart()
    {
        return numOfItemsInCart;
    }

    public List<ProductInfo> getRecommendation()
    {
        return recommendation;
    }

    public List<Address> getShippingAddresses()
    {
        return shippingAddresses;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public void setCustomerPaymentInfoId(String customerPaymentInfoId)
    {
        this.customerPaymentInfoId = customerPaymentInfoId;
    }

    public void setCartId(String cartId)
    {
        this.cartId = cartId;
    }

    public void setCustomerFeedId(String customerFeedId)
    {
        this.customerFeedId = customerFeedId;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setNumOfItemsInCart(int numOfItemsInCart)
    {
        this.numOfItemsInCart = numOfItemsInCart;
    }

    public void setRecommendation(List<ProductInfo> recommendation)
    {
        this.recommendation = recommendation;
    }

    public void setShippingAddresses(List<Address> shippingAddresses)
    {
        this.shippingAddresses = shippingAddresses;
    }
}
