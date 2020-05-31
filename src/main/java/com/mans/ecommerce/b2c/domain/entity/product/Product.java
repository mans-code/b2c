package com.mans.ecommerce.b2c.domain.entity.product;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.product.subEntity.Availability;
import com.mans.ecommerce.b2c.domain.entity.product.subEntity.BasicInfo;
import com.mans.ecommerce.b2c.domain.entity.product.subEntity.Details;
import com.mans.ecommerce.b2c.domain.entity.product.subEntity.Feedbacks;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {})
public class Product
{
    @Id
    private String id;

    private List<String> keyWords; //for search

    private BasicInfo basicInfo;

    private Details details;

    private Availability availability;

    private Feedbacks feedback;

    private List<ProductInfo> similarItems;

    @Version
    Long version;

}
