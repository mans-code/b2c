package com.mans.ecommerce.b2c.domain.entity.product;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mans.ecommerce.b2c.domain.entity.product.subEntity.*;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import lombok.*;
import org.springframework.data.annotation.Id;
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
    @Id
    private String id;

    @Indexed(unique = true)
    @JsonProperty("sku")
    private String mainSku;

    private BasicInfo basicInfo;

    private Details details;

    private Availability availability;

    private Feedbacks feedback;

    private Map<String, List<Variation>> variations;

    private List<ProductInfo> similarItems;

}
