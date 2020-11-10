package com.mans.ecommerce.b2c.controller.utill.dto;

import javax.validation.constraints.NotBlank;

import com.mans.ecommerce.b2c.domain.entity.customer.subEntity.Address;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CheckoutDto
{

    private Address address;

    @NotBlank
    private String token;

    private String shippingMethodId;
}
