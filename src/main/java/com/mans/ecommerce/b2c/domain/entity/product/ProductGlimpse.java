package com.mans.ecommerce.b2c.domain.entity.product;

import java.util.Date;
import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.Price;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = {})
public class ProductGlimpse
{

    private String id;

    private List<String> keyWords; //for search

    private String title;

    private String imageUrl;

    private Price cheapestUnitPrice;

    private double avgRatingScore;

    private int numCustomerRatings;

    private int numUnitsAvailable;

    private Date availableOn;

    private List<String> shipTo;
}

