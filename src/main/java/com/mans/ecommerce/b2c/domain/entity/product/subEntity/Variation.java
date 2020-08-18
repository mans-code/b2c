package com.mans.ecommerce.b2c.domain.entity.product.subEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Variation
{
    private Availability availability;

    private BasicInfo basicInfo;

    private Details details;

    private int notify;
}
