package com.mans.ecommerce.b2c.e2e;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class NotActiveCartControllerIT
{

    private final String BASE_URL = "/carts/{id}";

    private final String VALID_CART_ID = "5eaa32339e58d82df4319996";

    private final String INVALID_CART_ID = "5eaa32339e58d82df4319996";

    @Autowired
    private WebTestClient webTestClient;


    @Test
    public void add_pass()
    {

    }

    @Test
    public void update_pass()
    {

    }

    @Test
    public void remove_pass()
    {

    }

    @Test
    public void updateRemove_pass()
    {

    }

    @Test
    public void add_pass_PartialOutOfStock()
    {

    }

    @Test
    public void update_pass_PartialOutOfStock()
    {

    }

    @Test
    public void add_pass_outOfStock()
    {

    }

    @Test
    public void update_pass_outOfStock()
    {

    }
}
