package com.mans.ecommerce.b2c.controller.utill.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.mans.ecommerce.b2c.controller.utill.annotation.ValueOfEnum;
import com.mans.ecommerce.b2c.domain.enums.CartAction;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductDto
{

    @NotNull
    @NotBlank
    @Size(min = 8, message = "must be 8 or more characters in length")
    private String sku;

    private String variationId;

    private int quantity;

    @NotNull
    @NotBlank
    @ValueOfEnum(enumClass = CartAction.class)
    private CartAction cartAction;

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }
}
