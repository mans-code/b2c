package com.mans.ecommerce.b2c.domain.entity;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import com.mans.ecommerce.b2c.domain.entity.subEntity.Address;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString(exclude = {"password"})
@Document(collection = "customer")
public class Customer
{
    // validation , verification, and info for backend
    @Id
    private String id;

    private String username;

    private String password;

    private String phoneNumber;

    private String email;

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

    public Customer(String username, String password, String email, String name)
    {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
    }

}
