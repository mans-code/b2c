package com.mans.ecommerce.b2c.domain.enums;

public enum CartAction
{
    ADD("add"),
    UPDATE("update"),
    DELETE("delete"),
    RESET("reset");

    private String cartAction;

    CartAction(String cartAction)
    {
        this.cartAction = cartAction;
    }

    public String getCartAction()
    {
        return cartAction;
    }

}
