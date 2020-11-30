package com.mans.ecommerce.b2c.controller.customer;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.customer.subEntity.Address;
import com.mans.ecommerce.b2c.service.CustomerService;
import com.mans.ecommerce.b2c.utill.response.CheckoutResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/customers/{id}")
@Tag(name = "customer api", description = " get all the customer basic info")
public class CustomerController
{

    private CustomerService customerService;

    CustomerController(CustomerService customerService)
    {
        this.customerService = customerService;
    }

    @GetMapping(value = "/shipping")
    @Operation(description = "returns the customer shipping addresses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Address.class))),
            @ApiResponse(responseCode = "404", description = "product with the given id not found")
    })
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
