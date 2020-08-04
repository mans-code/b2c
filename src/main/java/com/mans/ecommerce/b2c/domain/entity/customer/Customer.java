package com.mans.ecommerce.b2c.domain.entity.customer;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.customer.subEntity.Address;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Customer
{

    @Id
    private String id;

    @Indexed
    private String username;

    private String password;

    private String phoneNumber;

    //Maybe Indexed
    private String email;

    private String firstName;

    private String lastName;

    private int numOfItemsInCart;

    private List<Address> shippingAddresses;

}
