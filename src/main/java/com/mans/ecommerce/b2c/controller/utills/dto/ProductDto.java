package com.mans.ecommerce.b2c.controller.utills.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductDto
{

    protected String sku;

    protected int quantity;

    protected String variationSku;

    protected final String SEPARATOR = "@";

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }
}
