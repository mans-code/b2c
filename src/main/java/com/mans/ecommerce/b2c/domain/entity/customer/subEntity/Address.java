package com.mans.ecommerce.b2c.domain.entity.customer.subEntity;

import javax.validation.constraints.NotBlank;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {})
public class Address
{

    private int id;

    @NotBlank
    private String name;

    @NotBlank
    private String address;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @NotBlank
    private String zip;

    private String phoneNumber;

    private String accessCode;

    private boolean def;
}
