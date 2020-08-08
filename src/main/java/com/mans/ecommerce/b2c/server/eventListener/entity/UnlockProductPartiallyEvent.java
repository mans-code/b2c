package com.mans.ecommerce.b2c.server.eventListener.entity;

import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bson.types.ObjectId;

@AllArgsConstructor
@Getter
public class UnlockProductPartiallyEvent
{

    private ObjectId cartId;

    private ProductInfo productInfo;

    private int toUnlock;

    private int newReservedQuantity;

}
