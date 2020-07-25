package com.mans.ecommerce.b2c.controller.utills.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.mans.ecommerce.b2c.controller.utills.annotation.ValueOfEnum;
import com.mans.ecommerce.b2c.domain.enums.CartAction;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductDto
{

    @NotEmpty
    @Size(min = 8, message = "must be 8 or more characters in length")
    private String sku;

    private String variationId;

    private int quantity;

    @ValueOfEnum(enumClass = CartAction.class)
    private CartAction cartAction;

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }
}
