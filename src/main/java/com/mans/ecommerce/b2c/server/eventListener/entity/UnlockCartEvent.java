package com.mans.ecommerce.b2c.server.eventListener.entity;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UnlockCartEvent
{
    private String cartId;

    private List<ProductInfo> productInfos;
}
