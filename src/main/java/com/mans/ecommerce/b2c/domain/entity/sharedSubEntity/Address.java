package com.mans.ecommerce.b2c.domain.entity.sharedSubEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address
{
    private String name;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String phoneNumber;
    private String accessCode;

    protected Address()
    {

    }
}
