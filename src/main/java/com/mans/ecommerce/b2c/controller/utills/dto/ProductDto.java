package com.mans.ecommerce.b2c.controller.utills.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductDto
{

    @JsonProperty("id")
    @NotEmpty
    @Size(min = 8, message = "must be 8 or more characters in length")
    protected String sku;

    protected int quantity;

    @JsonProperty("variationId")
    protected String variationSku;

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }
}
