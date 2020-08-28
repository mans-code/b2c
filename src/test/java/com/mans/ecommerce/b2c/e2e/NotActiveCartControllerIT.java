package com.mans.ecommerce.b2c.e2e;

import java.math.BigDecimal;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.mans.ecommerce.b2c.controller.utill.dto.ProductDto;
import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import com.mans.ecommerce.b2c.e2e.utill.CartTestUtill;
import com.mans.ecommerce.b2c.repository.product.ProductRepository;
import com.mans.ecommerce.b2c.service.CheckoutService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
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

    @SpyBean
    private CheckoutService checkoutService;

    private CartTestUtill cartTestUtill;

    @Test
    public void add_pass()
    {
        String cartId = "5eaa32339e58d82df43199a2";
        String sku = "mans-18";
        String cartAction = "add";

        BigDecimal productAmount = new BigDecimal("40");
        BigDecimal cartExpectedAmount = productAmount.multiply(BigDecimal.valueOf(3));

        ProductDto dto = cartTestUtill.getProductDto(sku, cartAction, 3);
        Map<String, Integer> infoExpectation = ImmutableMap.<String, Integer>builder().put(dto.getSku(), 3).build();

        cartTestUtill.callApiAndVerifyPassing(cartId, productAmount, cartExpectedAmount, dto, infoExpectation, true);
        verify(checkoutService, times(0)).lock(any(Cart.class), any(ProductInfo.class));
    }

    @Test
    public void remove_pass()
    {
        String cartId = "5eaa32339e58d82df43199a5";
        String sku = "mans-43";
        String cartAction = "delete";

        BigDecimal productAmount = new BigDecimal("40");
        BigDecimal cartExpectedAmount = productAmount.multiply(BigDecimal.valueOf(0));

        ProductDto dto = cartTestUtill.getProductDto(sku, cartAction, 3);
        Map<String, Integer> infoExpectation = ImmutableMap.<String, Integer>builder().put(dto.getSku(), 7).build();

        cartTestUtill.callApiAndVerifyPassing(cartId, productAmount, cartExpectedAmount, dto, infoExpectation, false);
        verify(checkoutService, times(0)).unlock(any(Cart.class), any(ProductInfo.class));
    }

    @Test
    public void updateAdd_pass()
    {

        String cartId = "5eaa32339e58d82df43199a7";
        String sku = "mans-2";
        String cartAction = "update";

        BigDecimal productAmount = new BigDecimal("24");
        BigDecimal cartExpectedAmount = new BigDecimal("333").add(productAmount.multiply(BigDecimal.valueOf(2)));

        ProductDto dto = cartTestUtill.getProductDto(sku, cartAction, 4);
        Map<String, Integer> infoExpectation = ImmutableMap.<String, Integer>builder().put(dto.getSku(), 4).build();

        cartTestUtill.callApiAndVerifyPassing(cartId, productAmount, cartExpectedAmount, dto, infoExpectation, true);
        verify(checkoutService, times(0)).partialLock(any(Cart.class), any(ProductInfo.class), anyInt());
    }

    @Test
    public void updateReduce_pass()
    {
        String cartId = "5eaa32339e58d82df43199c2";
        String sku = "mans-42";
        String cartAction = "update";
        int qty = 5;

        BigDecimal productAmount = new BigDecimal("50");
        BigDecimal cartExpectedAmount = new BigDecimal("670").subtract(productAmount.multiply(BigDecimal.valueOf(qty)));

        ProductDto dto = cartTestUtill.getProductDto(sku, cartAction, qty);
        Map<String, Integer> infoExpectation = ImmutableMap.<String, Integer>builder().put(dto.getSku(), qty).build();

        cartTestUtill.callApiAndVerifyPassing(cartId, productAmount, cartExpectedAmount, dto, infoExpectation, true);
        verify(checkoutService, times(0)).partialUnlock(any(Cart.class), any(ProductInfo.class), anyInt());
    }

    @Test
    public void updateRemove_pass()
    {
        String cartId = "5eaa32339e58d82df431999f";
        String sku = "mans-18";
        String cartAction = "update";

        BigDecimal productAmount = new BigDecimal("40");
        BigDecimal cartExpectedAmount = new BigDecimal("850").subtract(productAmount.multiply(BigDecimal.valueOf(3)));
        ProductDto dto = cartTestUtill.getProductDto(sku, cartAction, 0);
        Map<String, Integer> infoExpectation = ImmutableMap.<String, Integer>builder().put(dto.getSku(), 3).build();

        cartTestUtill.callApiAndVerifyPassing(cartId, productAmount, cartExpectedAmount, dto, infoExpectation, false);
        verify(checkoutService, times(0)).unlock(any(Cart.class), any(ProductInfo.class));
    }

    @Test
    public void add_pass_PartialOutOfStock()
    {
        String cartId = "5eaa32339e58d82df431999b";
        String sku = "mans-15";
        String cartAction = "add";

        BigDecimal productAmount = new BigDecimal("45");
        BigDecimal cartExpectedAmount = new BigDecimal("217").add(productAmount.multiply(BigDecimal.valueOf(5)));

        ProductDto dto = cartTestUtill.getProductDto(sku, cartAction, 10);
        Map<String, Integer> infoExpectation = ImmutableMap.<String, Integer>builder().put(dto.getSku(), 5).build();

        cartTestUtill.callApiAndVerifyPartialOutOfStock(cartId, productAmount, cartExpectedAmount, dto,
                                                        infoExpectation);
        verify(checkoutService, times(0)).lock(any(Cart.class), any(ProductInfo.class));
    }

    @Test
    public void update_pass_PartialOutOfStock()
    {
        String cartId = "5eaa32339e58d82df43199bd";
        String sku = "mans-15";
        String cartAction = "update";
        int availableQty = 5;

        BigDecimal productAmount = new BigDecimal("45");
        BigDecimal cartExpectedAmount = productAmount.multiply(BigDecimal.valueOf(availableQty))
                                                     .add(new BigDecimal("430"));

        ProductDto dto = cartTestUtill.getProductDto(sku, cartAction, 10);
        Map<String, Integer> infoExpectation = ImmutableMap.<String, Integer>builder().put(dto.getSku(), 9).build();

        cartTestUtill.callApiAndVerifyPartialOutOfStock(cartId, productAmount, cartExpectedAmount, dto,
                                                        infoExpectation);
        verify(checkoutService, times(0)).partialUnlock(any(Cart.class), any(ProductInfo.class), anyInt());
    }

    @Test
    public void add_pass_outOfStock()
    {
        String cartId = "5eaa32339e58d82df43199ba";
        outOfStock(cartId, "add");
        verify(checkoutService, times(0)).lock(any(Cart.class), any(ProductInfo.class));
    }

    @Test
    public void update_pass_outOfStock()
    {
        String cartId = "5eaa32339e58d82df43199ba";
        outOfStock(cartId, "update");
        verify(checkoutService, times(0)).partialLock(any(Cart.class), any(ProductInfo.class), anyInt());
    }

    private void outOfStock(String cartId, String cartAction)
    {
        String sku = "mans-46";
        ProductDto dto = cartTestUtill.getProductDto(sku, cartAction, 10);

        cartTestUtill.callApiAndVerifyOutOfStock(cartId, dto);
    }

    @Autowired
    public void cartControllerInstance(WebTestClient webTestClient, ProductRepository productRepository)
    {
        cartTestUtill = new CartTestUtill(webTestClient, productRepository, false);
    }

}
