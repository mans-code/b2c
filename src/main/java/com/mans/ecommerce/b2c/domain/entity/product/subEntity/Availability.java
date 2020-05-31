package com.mans.ecommerce.b2c.domain.entity.product.subEntity;

import javax.validation.constraints.Min;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {})
public class Availability
{

    private int numUnitsAvailable;

    private Date availableOn;

    private List<String> shipTo;
}
