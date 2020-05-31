package com.mans.ecommerce.b2c.controller.utills.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class ProductInfoDto
{
    //TODO product variation

    @NotBlank
    private String productId;

    @Min(1)
    @Max(20) //TODO find a good number
    private int quantity;

    private Map<String, String> variation;
}
