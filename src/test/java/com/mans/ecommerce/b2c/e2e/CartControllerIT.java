package com.mans.ecommerce.b2c.e2e;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.Money;
import com.mans.ecommerce.b2c.domain.enums.Currency;
import com.mans.ecommerce.b2c.domain.exception.ResourceNotFoundException;
import com.mans.ecommerce.b2c.utills.Global;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class CartControllerIT
{
    private final String BASE_URL = "/carts/{id}";

    private final String VALID_CART_ID = "5eaa32339e58d82df43199a2";

    private final String INVALID_CART_ID = "5f3f3ae7fc13ae2ebe000063";

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void getCart_pass()
    {
        webTestClient.get()
                     .uri(BASE_URL, VALID_CART_ID)
                     .accept(Global.JSON)
                     .exchange()
                     .expectStatus().is2xxSuccessful()
                     .expectBody(Cart.class)
                     .consumeWith(res -> {
                         Cart actual = res.getResponseBody();
                         Cart expected = getExpectedCart();
                         assertTrue(isEqual(actual, expected));
                     });
    }

    @Test
    public void getCart_fail_notFound()
    {
        webTestClient.get()
                     .uri(BASE_URL, INVALID_CART_ID)
                     .accept(Global.JSON)
                     .exchange()
                     .expectStatus().isBadRequest()
                     .expectBody(ResourceNotFoundException.class)
                     .consumeWith(res -> {
                         String expected = res.getResponseBody().getMessage();
                         assertThat(expected, equalToIgnoringCase("Cart Not Found"));
                     });
    }


    private boolean isEqual(Cart actual, Cart expected)
    {
        return actual.getId().equals(expected.getId())
                       &&
                       actual.isActive() == expected.isActive()
                       &&
                       actual.getTotalQuantity() == expected.getTotalQuantity()
                       &&
                       actual.isAnonymous() == expected.isAnonymous()
                       &&
                       actual.getMoney().getAmount().equals(expected.getMoney().getAmount())
                       &&
                       actual.getProductInfos().isEmpty() == expected.getProductInfos().isEmpty();
    }

    private Cart getExpectedCart()
    {
        return Cart.builder()
                   .id(new ObjectId(VALID_CART_ID))
                   .active(true)
                   .anonymous(false)
                   .productInfos(new ArrayList<>())
                   .money(new Money(new BigDecimal("0"), Currency.USD))
                   .build();
    }
}
