package com.mans.ecommerce.b2c.controller.utills.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class UpdateProductDto extends ProductDto
{

    @Min(value = 0, message = "must be 0 or more")
    @Max(value = 20, message = "must be no more than 20")
    private int quantity;

}
