package com.mans.ecommerce.b2c.e2e;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.mans.ecommerce.b2c.domain.entity.customer.subEntity.Address;
import com.mans.ecommerce.b2c.domain.exception.ResourceNotFoundException;
import com.mans.ecommerce.b2c.utills.Global;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import static java.util.stream.Collectors.toMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.samePropertyValuesAs;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class CustomerControllerIT
{
    @Autowired
    private WebTestClient webTestClient;

    private final String VALID_CUSTOMER_ID = "5eaa32339e58d82df4319992";

    private final String INVALID_CUSTOMER_ID = "5f3f3ae7fc13ae2ebe000063";

    private final String BASE_URL = "/customers/{id}";

    private final String SHIPPING_PATH = "/shipping";

    @Test
    public void getShippingAddresses_successes()
    {
        webTestClient.get()
                     .uri(BASE_URL + SHIPPING_PATH, VALID_CUSTOMER_ID)
                     .accept(Global.JSON)
                     .exchange()
                     .expectStatus().is2xxSuccessful()
                     .expectBodyList(Address.class)
                     .hasSize(2)
                     .consumeWith(res -> {
                         Map<String, Address> map = res.getResponseBody()
                                                       .stream()
                                                       .collect(toMap(Address::getId,
                                                                      Function.identity()));
                         getAddresses().forEach(expected -> {
                             Address got = map.get(expected.getId());
                             assertThat(expected, samePropertyValuesAs(got));
                         });
                     });
    }

    @Test
    public void getShippingAddresses_failed_CustomerNotFound()
    {
        webTestClient.get()
                     .uri(BASE_URL + SHIPPING_PATH, INVALID_CUSTOMER_ID)
                     .accept(Global.JSON)
                     .exchange()
                     .expectStatus().isBadRequest()
                     .expectBody(ResourceNotFoundException.class)
                     .consumeWith(res -> {
                         ResourceNotFoundException ex = res.getResponseBody();
                         assertThat(ex.getMessage(), equalToIgnoringCase("Customer not found"));
                     });
    }

    private List<Address> getAddresses()
    {
        Address first = Address.builder()
                               .id("30fe11e4-f66a-4cd6-af7f-cd9ec52bcf93")
                               .name("Padget Sandlin")
                               .address("5391 Magdeline Street")
                               .city("San Angelo")
                               .state("TX")
                               .zip("76905")
                               .def(true)
                               .build();

        Address second = Address.builder()
                                .id("38dfc048-9dbb-4892-978e-0666f93797c4")
                                .name("Mitzi MacKaig")
                                .address("50 Crowley Trail")
                                .city("Lansing")
                                .state("MI")
                                .zip("48930")
                                .def(false)
                                .build();

        return new ArrayList<>(Arrays.asList(first, second));
    }
}