package com.mans.ecommerce.b2c.server.eventListener.entity;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bson.types.ObjectId;

@AllArgsConstructor
@Getter
public class BoughtProductsEvent
{
    private ObjectId feedId;

    private List<ProductInfo> productInfos;
}
