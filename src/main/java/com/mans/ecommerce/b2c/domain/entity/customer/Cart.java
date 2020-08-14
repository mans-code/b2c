package com.mans.ecommerce.b2c.domain.entity.customer;

import java.time.LocalTime;
import java.util.List;

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

    private LocalTime expireDate;

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

    public String getId()
    {
        return id.toHexString();
    }

    public ObjectId getIdObj()
    {
        return id;
    }
}
