package com.mans.ecommerce.b2c.domain.entity.customer.subEntity;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {})
@Builder
public class Address
{

    private String id;

    @NotBlank
    @Schema(description = "package receiver full name")
    private String name;

    @NotBlank
    @Schema(description = "shipping full address")
    private String address;

    @NotBlank
    @Schema(description = "which city the address located at")
    private String city;

    @NotBlank
    @Schema(description = "which state the address located at")
    private String state;

    @NotBlank
    @Schema(description = "the address zip code")
    private String zip;

    @Schema(description = "package receiver phone number")
    private String phoneNumber;

    @Schema(description = "access code to enter the building if any")
    private String accessCode;

    @Schema(description = "let this address as the default address in future")
    private boolean def;
}
