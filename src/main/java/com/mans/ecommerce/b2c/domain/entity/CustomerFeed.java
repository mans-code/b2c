package com.mans.ecommerce.b2c.domain.entity;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.subEntity.ProductInfo;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class CustomerFeed
{
    @Id
    private String id;

    private String customerId;

    private List<ProductInfo> clicks;

    private List<ProductInfo> bought;

    private ProductInfo lastProductBought;

    private ProductInfo lastProductClicked;

    private List<ProductInfo> addedToChart;

    protected CustomerFeed()
    {

    }

    public String getId()
    {
        return id;
    }

    public String getCustomerId()
    {
        return customerId;
    }

    public List<ProductInfo> getClicks()
    {
        return clicks;
    }

    public List<ProductInfo> getBought()
    {
        return bought;
    }

    public ProductInfo getLastProductBought()
    {
        return lastProductBought;
    }

    public ProductInfo getLastProductClicked()
    {
        return lastProductClicked;
    }

    public List<ProductInfo> getAddedToChart()
    {
        return addedToChart;
    }
}
