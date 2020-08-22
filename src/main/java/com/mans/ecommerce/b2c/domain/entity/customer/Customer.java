package com.mans.ecommerce.b2c.domain.entity.customer;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.customer.subEntity.Address;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Customer
{

    @Id
    private ObjectId id;

    private String username;

    @CreatedDate
    private LocalDateTime createdOn;

    private String password;

    private String phoneNumber;

    private String email;

    private String firstName;

    private String lastName;

    private int numOfItemsInCart;

    private String token;

    private boolean enabled;

    private List<Address> shippingAddresses;

    public String getId()
    {
        return id.toHexString();
    }
}
