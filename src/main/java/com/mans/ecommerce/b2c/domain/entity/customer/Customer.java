package com.mans.ecommerce.b2c.domain.entity.customer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {})
public class Customer
{

    @Id
    private String id;

    private String username;

    private String password;

    private String phoneNumber;

    @Indexed
    private String email;

    private String firstName;

    private String lastName;

    private int numOfItemsInCart;

    public Customer(String username, String password, String email, String firstName, String lastName)
    {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
