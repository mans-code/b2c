package com.mans.ecommerce.b2c.controller.utills.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public abstract class ProductDto
{

    @NotEmpty
    @Size(min = 8, message = "must be 8 or more characters in length")
    protected String sku;

    protected int quantity;

    protected final String SEPARATOR = "@";

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }
}
