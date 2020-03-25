package com.mans.ecommerce.b2c.domain.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
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

    public void setId(String productId)
    {
        this.productId = productId;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public void setCheapestUnitPrice(double cheapestUnitPrice)
    {
        this.cheapestUnitPrice = cheapestUnitPrice;
    }

    public void setAvgRatingScore(double avgRatingScore)
    {
        this.avgRatingScore = avgRatingScore;
    }

    public void setNumCustomerRatings(int numCustomerRatings)
    {
        this.numCustomerRatings = numCustomerRatings;
    }

    public void setNumUnitsAvailable(int numUnitsAvailable)
    {
        this.numUnitsAvailable = numUnitsAvailable;
    }

    public void setAvailableOn(Date availableOn)
    {
        this.availableOn = availableOn;
    }

    public String getProductId()
    {
        return productId;
    }

    public String getTitle()
    {
        return title;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public double getCheapestUnitPrice()
    {
        return cheapestUnitPrice;
    }

    public double getAvgRatingScore()
    {
        return avgRatingScore;
    }

    public int getNumCustomerRatings()
    {
        return numCustomerRatings;
    }

    public int getNumUnitsAvailable()
    {
        return numUnitsAvailable;
    }

    public Date getAvailableOn()
    {
        return availableOn;
    }

    public List<String> getShipTo()
    {
        return shipTo;
    }

    public String getProjectDetailsId()
    {
        return projectDetailsId;
    }

    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    public void setShipTo(List<String> shipTo)
    {
        this.shipTo = shipTo;
    }

    public void setProjectDetailsId(String projectDetailsId)
    {
        this.projectDetailsId = projectDetailsId;
    }

    public void setKeyWords(List<String> keyWords)
    {
        this.keyWords = keyWords;
    }

    public List<String> getKeyWords()
    {
        return keyWords;
    }
}

