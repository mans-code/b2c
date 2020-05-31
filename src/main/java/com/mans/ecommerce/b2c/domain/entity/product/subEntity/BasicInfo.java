package com.mans.ecommerce.b2c.domain.entity.product.subEntity;

import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.Price;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {})
public class BasicInfo
{
    private String title;

    private String by;

    private Price priceGlimpse;

    private String mainImageUrl;
}
