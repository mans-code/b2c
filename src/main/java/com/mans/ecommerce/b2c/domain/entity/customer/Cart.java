package com.mans.ecommerce.b2c.domain.entity.customer;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.Money;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import lombok.*;
import org.bson.types.ObjectId;
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
public class Cart implements Cloneable
{
    @Id
    private ObjectId id;

    private boolean active;

    private Date expireDate;

    private List<ProductInfo> productInfos;

    private Money money;

    private int totalQuantity;

    private boolean anonymous;

    @Version
    private long version;

    public Cart(boolean anonymous)
    {
        this.anonymous = anonymous;
    }

    public Cart(Cart cart)
    {
        id = cart.id;
        active = cart.active;
        expireDate = new Date(cart.expireDate.getTime());
        money = new Money(cart.money);
        totalQuantity = cart.totalQuantity;
        productInfos = cart.getProductInfos()
                           .stream()
                           .map(info -> new ProductInfo(info))
                           .collect(Collectors.toList());
    }

    public String getId()
    {
        return id.toHexString();
    }

    public ObjectId getIdObj()
    {
        return id;
    }
}
