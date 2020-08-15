package com.mans.ecommerce.b2c.controller.utills.dto;

import com.mans.ecommerce.b2c.domain.entity.customer.subEntity.Address;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CheckoutDto
{

    private Address address;

    private String token;

    private String shippingMethodId;
}
