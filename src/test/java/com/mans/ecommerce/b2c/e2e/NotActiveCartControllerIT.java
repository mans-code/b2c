package com.mans.ecommerce.b2c.e2e;

import java.math.BigDecimal;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.mans.ecommerce.b2c.controller.utill.dto.ProductDto;
import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import com.mans.ecommerce.b2c.service.CheckoutService;
import com.mans.ecommerce.b2c.utills.Global;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class NotActiveCartControllerIT
{

    private final String BASE_URL = "/carts/{id}";

    private final String VALID_CART_ID = "5eaa32339e58d82df4319996";

    private final String INVALID_CART_ID = "5eaa32339e58d82df4319996";

    @SpyBean
    private CheckoutService checkoutService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void add_pass()
    {

        verify(checkoutService, times(0)).lock(any(Cart.class), any(ProductInfo.class));
    }

    @Test
    public void remove_pass()
    {

        verify(checkoutService, times(0)).unlock(any(Cart.class), any(ProductInfo.class));
    }

    @Test
    public void updateAdd_pass()
    {
        verify(checkoutService, times(0)).partialLock(any(Cart.class), any(ProductInfo.class), anyInt());
    }

    @Test
    public void updateReduce_pass()
    {
        verify(checkoutService, times(0)).partialUnlock(any(Cart.class), any(ProductInfo.class), anyInt());
    }

    @Test
    public void updateRemove_pass()
    {
        verify(checkoutService, times(0)).unlock(any(Cart.class), any(ProductInfo.class));
    }

    @Test
    public void add_pass_PartialOutOfStock()
    {
        verify(checkoutService, times(0)).lock(any(Cart.class), any(ProductInfo.class));
    }

    @Test
    public void update_pass_PartialOutOfStock()
    {
        verify(checkoutService, times(0)).partialUnlock(any(Cart.class), any(ProductInfo.class), anyInt());
    }

    @Test
    public void add_pass_outOfStock()
    {
        verify(checkoutService, times(0)).lock(any(Cart.class), any(ProductInfo.class));
    }

    @Test
    public void update_pass_outOfStock()
    {

        verify(checkoutService, times(0)).partialLock(any(Cart.class), any(ProductInfo.class), anyInt());
    }



}
