package com.mans.ecommerce.b2c.controller.utill.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.mans.ecommerce.b2c.controller.utill.annotation.ValueOfEnum;
import com.mans.ecommerce.b2c.domain.enums.CartAction;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductDto
{

    @NotBlank(message = "must not be empty")
    @Size(min = 8, message = "must be 8 or more characters in length")
    private String sku;

    private String variationId;

    @Min(value = -1, message = "must be -1 or more")
    @Max(value = 20, message = "must be no more than 20")
    private int quantity;

    @NotBlank(message = "must not be empty")
    @ValueOfEnum(enumClass = CartAction.class)
    private String cartAction;

    public ProductDto(String sku, String cartAction)
    {
        this.sku = sku;
        this.cartAction = cartAction;
    }

    public ProductDto(String sku, String cartAction, int quantity)
    {
        this.sku = sku;
        this.cartAction = cartAction;
        this.quantity = quantity;
    }

    public CartAction getCartAction()
    {
        return CartAction.valueOf(cartAction);
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }
}
