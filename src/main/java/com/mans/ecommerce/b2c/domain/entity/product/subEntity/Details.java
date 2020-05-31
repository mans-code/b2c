package com.mans.ecommerce.b2c.domain.entity.product.subEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {})
public class Details
{
    private String shortDescription; // HTML

    private String productInformation; //  HTML

    private String fromManufacturer; // HTML

    private String description; // HTML
}
