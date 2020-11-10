package com.mans.ecommerce.b2c.domain.entity.customer;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.Money;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import com.mans.ecommerce.b2c.domain.enums.Currency;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
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

    public Cart(boolean anonymous)
    {
        this.anonymous = anonymous;
        init();
    }

    public Cart(){
        init();
    }

    private void init()
    {
        productInfos = new ArrayList<>();
        money = new Money(BigDecimal.ZERO, Currency.USD);
    }
}
