package com.mans.ecommerce.b2c.controller.customer;

import javax.validation.constraints.NotNull;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.customer.subEntity.Address;
import com.mans.ecommerce.b2c.service.CustomerService;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/customers/{id}")
public class CustomerController
{

    private CustomerService customerService;

    CustomerController(CustomerService customerService)
    {
        this.customerService = customerService;
    }

    @GetMapping(value = "/shipping")
    public Mono<List<Address>> getShippingAddresses(@PathVariable("id") @NotNull ObjectId id)
    {
        return customerService.getShippingAddresses(id)
                              .switchIfEmpty(Mono.just(new ArrayList<>()));
    }

    private List<Address> emptyAddressesList()
    {
        Address fake = Address.builder().name("Mans").address("1092 Sandhurst").city("Toronto").build();
        return  new ArrayList<Address>(
                Arrays.asList(fake));
    }

}
