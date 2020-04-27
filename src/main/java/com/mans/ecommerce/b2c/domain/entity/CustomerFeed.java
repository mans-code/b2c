package com.mans.ecommerce.b2c.domain.entity;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString(exclude = {})
@Document(collection = "customer")
public class CustomerFeed
{
    @Id
    private String id;

    private String customerId;

    private List<ProductInfo> clicks;

    private List<ProductInfo> bought;

    private ProductInfo lastProductBought;

    private ProductInfo lastProductClicked;

    private List<ProductInfo> addedToChart;

    protected CustomerFeed()
    {

    }

}
