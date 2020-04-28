package com.mans.ecommerce.b2c.domain.entity.customer;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = {})
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

    public Address(
            String name,
            String address,
            String city,
            String state,
            String zip,
            String phoneNumber,
            String accessCode)
    {
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phoneNumber = phoneNumber;
        this.accessCode = accessCode;
    }
}
