package com.mans.ecommerce.b2c.controller.utills.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AddProductDto extends ProductDto
{

    @JsonProperty("id")
    @NotEmpty
    @Size(min = 8, message = "must be 8 or more characters in length")
    private String sku;

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
        if (variationSku == null && variation != null)
        {
            variationSku = construct();
        }
        return super.getVariationSku();
    }

    private String construct()
    {
        String dash = "-";
        StringBuilder str = new StringBuilder();

        variation.forEach((k, v) -> str.append(dash + v));
        str.append(SEPARATOR);
        str.append(sku);
        return str.toString();
    }
}
