package com.mans.ecommerce.b2c.domain.entity.product.subEntity;

import java.time.LocalDateTime;
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

    private LocalDateTime availableOn;

    private List<String> shipTo;
}
