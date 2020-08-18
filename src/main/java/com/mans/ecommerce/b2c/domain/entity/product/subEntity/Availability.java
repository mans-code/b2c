package com.mans.ecommerce.b2c.domain.entity.product.subEntity;

import java.time.LocalDate;
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

    private LocalDate availableOn;

    private List<String> shipTo;
}
