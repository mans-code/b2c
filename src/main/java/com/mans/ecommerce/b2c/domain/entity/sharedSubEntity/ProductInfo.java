package com.mans.ecommerce.b2c.domain.entity.subEntity;

import java.util.List;

public class ProductInfo
{
    private String productDetailId;
    private String productName;
    private String imageUrl;
    private int quantity;
    private double price;
    private List<String> availableCombination;

    protected  ProductInfo()
    {
    }


}
