package com.mans.ecommerce.b2c.controller.utill.dto;

import javax.validation.constraints.NotBlank;

import com.mans.ecommerce.b2c.domain.entity.customer.subEntity.Address;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Schema(description = "checkout detail to complete the cart payment and shipping")
public class CheckoutDto
{
    @Schema(description = "customer address", example = "123 Fake Street Saudi Arabia 75474")
    private Address address;

    @NotBlank
    @Schema(description = "token provided by stripe API")
    private String token;

    @Schema(description = "shipping method id")
    private String shippingMethodId;
}
