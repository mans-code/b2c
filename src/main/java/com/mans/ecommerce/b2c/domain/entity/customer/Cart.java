package com.mans.ecommerce.b2c.domain.entity.customer;

import java.util.Date;
import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.Money;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString(exclude = {})
public class Cart
{
    @Id
    private String id;

    private boolean active;

    private Date expireDate;

    private List<ProductInfo> productInfos;

    private Money money;

    private int totalQuantity;

    @Version
    private long version;
}
