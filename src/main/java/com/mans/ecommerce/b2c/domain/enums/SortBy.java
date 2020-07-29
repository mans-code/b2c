package com.mans.ecommerce.b2c.domain.enums;

public enum SortBy
{
    HELPFUL("helpful"),
    RECENT("createdOn");

    private String sortBy;

    SortBy(String sortBy)
    {
        this.sortBy = sortBy;
    }

    public String getSortBy()
    {
        return sortBy;
    }
}
