package com.mans.ecommerce.b2c.controller.customer;

import javax.validation.constraints.NotBlank;
import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.customer.subEntity.Address;
import com.mans.ecommerce.b2c.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customer/{id}")
public class CustomerController
{

    private CustomerService customerService;

    CustomerController(CustomerService customerService)
    {
        this.customerService = customerService;
    }

    @GetMapping("/shipping")
    public List<Address> getShippingAddresses(@PathVariable("idd") @NotBlank String id)
    {
        return customerService.getShippingAddresses(id);
    }

}
