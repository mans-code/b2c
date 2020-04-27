package com.mans.ecommerce.b2c.domain.entity.product;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString(exclude = {})
@Document(collection = "product_detail")
public class ProductDetails
{
    // maybe for every variation comes with description part;
    //private  manufacturerInfo; next resales

    private String productId;

    // info about the product is recommended to be html
    private String shortDescription; // CBH

    private String productInformation; //  CBH

    private String fromManufacturer; // CBH

    private String description;

    //Customer
    private int numOfQuestions;

    private List<QAndA> topQAndA;

    private int numReviews;

    private List<Review> topReviews;

    // price
    private String priceType; // TODO enum

    private double price;

    private String priceCurrency; //TODO enum

    private ProductVariation productVariations;

    private List<ProductGlimpse> similarItems;

    private class ProductVariation
    {
        private Map<String, List<String>> variation; // key:values

        private List<AvailableVariation> availableVariation;

        private class AvailableVariation
        {
            private Map<String, String> variation; // key:value  keySets will correspond to whole  keySets defined at ProductVariation variation

            private double price;

            private int quantity;

            private String description;

            private List<String> imagesUrl;
        }
    }
}
