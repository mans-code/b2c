package com.mans.ecommerce.b2c.domain.enums;

public enum PricingStrategy
{
    STATIC("static"),
    DYNAMIC("dynamic");

    private String pricingStrategy;

    PricingStrategy(String pricingStrategy)
    {
        this.pricingStrategy = pricingStrategy;
    }

    public String getPricingStrategy()
    {
        return pricingStrategy;
    }
}
