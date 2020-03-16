package com.mans.ecommerce.b2c.domain.entity;

import java.net.URI;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ProductGlimpse
{
    @Id
    private String id;
    private String name;
    private String title;
    private String imageUrl;
    private double cheapestUnitPrice;
    private double avgRatingScore;
    private int numCustomerRatings;
    private int numUnitsAvailable;
    private Date availableOn;
    private List<String> shipTo;

    private URI projectDetailsLink;//use mapping //TODO

    protected ProductGlimpse()
    {
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
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

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
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

}
