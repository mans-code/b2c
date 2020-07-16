package com.mans.ecommerce.b2c.utill;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewCustomerResponse
{
    private String customerId;
    private String token;
}
