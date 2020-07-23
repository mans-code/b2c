package com.mans.ecommerce.b2c.controller.utills.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AddProductDto extends ProductDto
{
    @Min(value = 1, message = "must be 1 or more")
    @Max(value = 20, message = "must be no more than 20")
    private int quantity;

    private TreeMap<String, String> variation;

    @JsonProperty
    public void setVariation(Map<String, String> map)
    {
        this.variation = new TreeMap<>(map);
    }

    @Override
    public String getVariationSku()
    {
        if (variationSku == null && variation != null )
        {
            variationSku = construct();
        }
        return super.getVariationSku();
    }

    private String construct()
    {
        String underscore = "_";
        StringBuilder str = new StringBuilder();
        str.append(sku);

        variation.forEach((k, v) -> str.append(underscore + v));

        return str.toString();
    }
}
