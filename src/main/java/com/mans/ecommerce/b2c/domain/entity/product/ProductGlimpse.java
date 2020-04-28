package com.mans.ecommerce.b2c.domain.entity.product;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString(exclude = {})
@Document(collection = "product_glimpse")
public class ProductGlimpse
{

    private String productId;

    private List<String> keyWords; //for search

    private String title;

    private String imageUrl;

    private double cheapestUnitPrice;

    private double avgRatingScore;

    private int numCustomerRatings;

    private int numUnitsAvailable;

    private Date availableOn;

    private List<String> shipTo;

    private String projectDetailsId;//use mapping //TODO

    protected ProductGlimpse()
    {
    }

    public ProductGlimpse(String title)
    {
        this.title = title;
    }
}

