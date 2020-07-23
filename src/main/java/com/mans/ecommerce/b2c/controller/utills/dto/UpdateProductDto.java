package com.mans.ecommerce.b2c.controller.utills.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateProductDto extends ProductDto
{

    @Min(value = 0, message = "must be 0 or more")
    @Max(value = 20, message = "must be no more than 20")
    private int quantity;

    @JsonProperty("id")
    @NotEmpty
    @Size(min = 8, message = "must be 8 or more characters in length")
    protected String variationSku;

    @Override public String getSku()
    {
        if (sku == null)
        {
            sku = extract();
        }
        return super.getSku();
    }

    private String extract()
    {
        return variationSku.split(SEPARATOR)[1];
    }
}
