package com.mans.ecommerce.b2c.domain.entity.customer.subEntity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {})
public class Address
{

    private int id;

    private String name;

    private String address;

    private String city;

    private String state;

    private String zip;

    private String phoneNumber;

    private String accessCode;

    private boolean def;
}
