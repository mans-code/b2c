package com.mans.ecommerce.b2c.domain.entity;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.subEntity.ProductInfo;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class customerFeed
{
    private String customerId;
    private List<ProductInfo> clicks;
    private List<ProductInfo> bought;
    private ProductInfo lastProductBought;
    private ProductInfo lastProductClicked;
    private List<ProductInfo> addedToChart;

}
