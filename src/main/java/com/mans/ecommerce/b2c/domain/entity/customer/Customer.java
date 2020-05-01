package com.mans.ecommerce.b2c.domain.entity.customer;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = {})
public class Customer
{

    private String id;

    private String username;

    private String password;

    private String phoneNumber;

    private String email;

    private String firstName;

    private String lastName;

    private int numOfItemsInCart;

    private List<ProductInfo> recommendations;
}
