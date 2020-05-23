package com.mans.ecommerce.b2c.domain.entity.customer;

import java.util.Date;
import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.Price;
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
public class Cart
{
    @Id
    private String id;

    private boolean active;

    private Date expireDate;

    private List<ProductInfo> productInfo;

    private Price totalPrice;

    private int totalQuantity;
}
