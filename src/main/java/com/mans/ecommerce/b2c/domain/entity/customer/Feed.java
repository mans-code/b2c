package com.mans.ecommerce.b2c.domain.entity.customer;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {})
public class Feed
{

    @Id
    private String id;

    private List<ProductInfo> clicks;

    private List<ProductInfo> bought;

    private List<ProductInfo> addedToChart;

    private ProductInfo lastProductBought;

    private ProductInfo lastProductClicked;

}
