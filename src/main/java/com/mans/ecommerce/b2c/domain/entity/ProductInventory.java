package com.mans.ecommerce.b2c.domain.entity;

public class ProductInventory
{

    private double weight;

    private String weightUnit; // TODO enum

    private String supplyId;

    private String supplierProductId; // The Vendor's Product ID (could be SKU or their own system). (Could be a duplication of SKU above.)

    private String name;

    private double msrp; //Manufacturer's Suggested Retail Price

    private double reorderLevel;

    private boolean productAvailable;

    private String note;

    private String category;

    private Discount discount;

    private String notificationType; //TODO enum

    //performance and verification
    private String productDetailsId;

    private String productGlimpseId;

    private class Discount
    {
        private String discountType;//TODO enum fixed dynamic

        private double discount;

        private double marginOfProfit; // %

        private double operationCost;// %

        private boolean discountAvailable;
    }

}
