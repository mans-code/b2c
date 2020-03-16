package com.mans.ecommerce.b2c.domain.entity.subEntity;

public class Address
{
    private String name;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String phoneNumber;
    private String accessCode;

    protected Address()
    {

    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public void setZip(String zip)
    {
        this.zip = zip;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public void setAccessCode(String accessCode)
    {
        this.accessCode = accessCode;
    }

    public String getName()
    {
        return name;
    }

    public String getAddress()
    {
        return address;
    }

    public String getCity()
    {
        return city;
    }

    public String getState()
    {
        return state;
    }

    public String getZip()
    {
        return zip;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public String getAccessCode()
    {
        return accessCode;
    }
}
