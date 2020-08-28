package com.mans.ecommerce.b2c.e2e.utill;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.ImmutableMap;
import com.mans.ecommerce.b2c.controller.utill.dto.ProductDto;
import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import com.mans.ecommerce.b2c.repository.product.ProductRepository;
import com.mans.ecommerce.b2c.utills.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CartController
{

    private boolean active;

    private final String errorMessageTemplate = "%s product info with sku=%s \n variationId=%s \n qty=%s \n amount=%s";

    private ProductLockingValidator lockingValidator;

    private final String BASE_URL = "/carts/{cartId}";

    @Autowired
    private WebTestClient webTestClient;

    public void callApiAndVerifyPassing(
            String cartId,
            BigDecimal productAmount,
            BigDecimal cartExpectedAmount,
            ProductDto dto,
            Map<String, Integer> productExpectation,
            Map<String, Integer> resExpectation,
            boolean productInfo,
            boolean res)
    {
        webTestClient.patch()
                     .uri(BASE_URL, cartId)
                     .body(Mono.just(dto), ProductDto.class)
                     .accept(Global.JSON)
                     .exchange()
                     .expectStatus().is2xxSuccessful()
                     .expectBody(Cart.class)
                     .consumeWith(response -> {
                         Cart cart = response.getResponseBody();

                         assertCartActivationStatus(cart);
                         assertTrue("cart total not the expected",
                                    cart.getMoney().getAmount().equals(cartExpectedAmount));

                         verifyCartProductInfo(cart, resExpectation, productAmount, productInfo);

                         if (active)
                         {
                             lockingValidator.productQuantityAndHasReservation(productExpectation,
                                                                               resExpectation,
                                                                               cartId,
                                                                               true);
                         }
                     });
    }

    private void verifyCartProductInfo(
            Cart cart,
            Map<String, Integer> expectation,
            BigDecimal productAmount,
            boolean addOrUpdate)
    {
        expectation.forEach((k, v) -> {
            System.out.println(v);
            Optional<ProductInfo> opt = cart.getProductInfos()
                                            .stream()
                                            .filter(info ->
                                                            info.getSku().equalsIgnoreCase(k)
                                                                    &&
                                                                    info.getVariationId().equalsIgnoreCase(k)
                                                                    && info.getQuantity() == v
                                                                    && info.getMoney().getAmount().equals(productAmount)
                                            ).findFirst();
            if (addOrUpdate)
            {
                assertTrue(String.format(errorMessageTemplate, "no", k, k, v, productAmount),
                           opt.isPresent());
            }
            else
            {
                assertFalse(String.format(errorMessageTemplate, "", k, k, v, productAmount),
                            opt.isPresent());
            }

        });
    }

    private ProductDto getProductDto(String sku, String cartAction, int lock)
    {
        return ProductDto.builder()
                         .sku(sku)
                         .variationId(sku)
                         .cartAction(cartAction)
                         .quantity(lock)
                         .build();
    }

    private void assertCartActivationStatus(Cart cart)
    {
        if (active)
        {
            assertTrue("Cart is not active ", cart.isActive());
        }
        else
        {
            assertFalse("Cart is active ", cart.isActive());
        }
    }

    @Autowired
    public void lockingValidatorInstance(ProductRepository productRepository)
    {
        lockingValidator = new ProductLockingValidator(productRepository);
    }
}
