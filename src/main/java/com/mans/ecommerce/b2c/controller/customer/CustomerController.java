package com.mans.ecommerce.b2c.controller.customer;

import javax.validation.constraints.NotNull;
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
@RequestMapping("/customer/{id}")
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
        return customerService.getShippingAddresses(id);
    }

}
