package com.mans.ecommerce.b2c.controller.utills.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Map;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ProductInfoDto
{
    //TODO product variation

    @NotEmpty
    private String productId;

    @Min(1)
    @Max(20) //TODO find a good number
    private int quantity;

    private Map<String, String> variation;
}
