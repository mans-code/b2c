package com.mans.ecommerce.b2c.e2e;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.customer.subEntity.Address;
import com.mans.ecommerce.b2c.utills.Global;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest
public class CustomerControllerIT
{

    private final String BASE_URL = "/customer/{id}";

    @Autowired WebTestClient webTestClient;

    @Test
    public void getShippingAddresses_success()
    {
//        Mono<List<Address>> addressesMono =
//                webTestClient.get()
//                             .uri(BASE_URL + "/shipping")
//                             .accept(Global.JSON)
//                            .exchange();

    }
}
