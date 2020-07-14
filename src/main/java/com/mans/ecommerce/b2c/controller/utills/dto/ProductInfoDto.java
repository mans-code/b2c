package com.mans.ecommerce.b2c.controller.utills.dto;

import javax.validation.constraints.*;
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
    @Size(min = 8, message = "must be 8 or more characters in length")
    private String productId;

    @Min(value = 1 , message = "must be 1 or more")
    @Max(value = 20, message = "must be no more than 20")
    private int quantity;

    private Map<String, String> variation;
}
