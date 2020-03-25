package com.mans.ecommerce.b2c.domain.entity.sharedSubEntity;

import java.util.Map;

public class ProductInfo
{
    private String productDetailId;

    private String productName;

    private String imageUrl;

    private int quantity;

    private double price;

    private Map<String, String> variation;

    protected ProductInfo()
    {
    }

    public String getProductDetailId()
    {
        return productDetailId;
    }

    public String getProductName()
    {
        return productName;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public double getPrice()
    {
        return price;
    }

    public Map<String, String> getVariation()
    {
        return variation;
    }

    public void setProductDetailId(String productDetailId)
    {
        this.productDetailId = productDetailId;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public void setVariation(Map<String, String> variation)
    {
        this.variation = variation;
    }
}
