package com.mans.ecommerce.b2c.domain.entity.customer;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.Money;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
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

    @CreatedDate
    private LocalDateTime createdOn;

    private Instant expireDate;

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
}
