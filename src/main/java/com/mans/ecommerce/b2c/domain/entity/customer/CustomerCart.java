package com.mans.ecommerce.b2c.domain.entity.customer;

import java.util.Date;
import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.Price;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = {})
public class CustomerCart
{
    private String id;

    private boolean active;

    private Date expireDate;

    private List<ProductInfo> productInfo;

    private Price totalPrice;

    private int totalQuantity;
}
