package com.mans.ecommerce.b2c.domain.entity.product.subEntity;

import java.util.Date;
import java.util.List;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString(exclude = {})
public class Availability
{

    private int quantity;

    private Date availableOn;

    private List<String> shipTo;
}
