package com.mans.ecommerce.b2c.domain.entity.product;

import java.util.List;
import java.util.Map;

import com.mans.ecommerce.b2c.domain.entity.product.subEntity.*;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString(exclude = {})
public class Product
{
    @Indexed(unique = true)
    private String sku;

    private List<VariationLevel> variationsLevels;

    private Map<String, Variation> variationsDetails;

    private Feedbacks feedback;

    private List<ProductInfo> similarItems;

    private List<Reservation> reservations;
}
