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

    private boolean built;

    @JsonProperty
    public void setVariation(Map<String, String> map)
    {
        this.variation = new TreeMap<>(map);
    }

    public String getSku()
    {
        if (!built && variation != null)
        {
            built = true;
            sku = build();
        }
        return sku;
    }

    private String build()
    {
        String dash = "-";
        StringBuilder str = new StringBuilder();

        variation.forEach((k, v) -> str.append(dash).append(v));
        str.append(SEPARATOR);
        str.append(sku);

        return str.toString();
    }
}
