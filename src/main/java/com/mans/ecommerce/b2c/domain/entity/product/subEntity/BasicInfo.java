package com.mans.ecommerce.b2c.domain.entity.product.subEntity;

import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.Money;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.Price;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString(exclude = {})
public class BasicInfo
{
    private String title;

    private String by;

    private Money money;

    private String imageUrl;
}
