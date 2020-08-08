package com.mans.ecommerce.b2c.server.eventListener.entity;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@AllArgsConstructor
@Getter
@Setter
public class UnlockCartEvent
{
    private ObjectId cartId;

    private List<ProductInfo> productInfos;
}
