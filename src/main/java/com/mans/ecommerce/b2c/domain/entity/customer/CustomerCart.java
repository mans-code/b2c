package com.mans.ecommerce.b2c.domain.entity.customer;

import java.util.Date;
import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString(exclude = { "id", "expireDate" })
@Document(collection = "cart")
public class CustomerCart
{
    @Id
    private String id;

    private String customerId;

    private boolean active;

    private Date expireDate;

    private List<ProductInfo> productInfo;

    private double totalPrice;

    private int totalQuantity;

    protected CustomerCart()
    {

    }

    public CustomerCart(
            String id,
            boolean active,
            Date expireDate,
            double totalPrice,
            int totalQuantity)
    {
        this.id = id;
        this.active = active;
        this.expireDate = expireDate;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
    }
}
