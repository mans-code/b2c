package com.mans.ecommerce.b2c.e2e.utill;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import com.mans.ecommerce.b2c.controller.utill.dto.ProductDto;
import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import com.mans.ecommerce.b2c.domain.exception.OutOfStockException;
import com.mans.ecommerce.b2c.domain.exception.PartialOutOfStockException;
import com.mans.ecommerce.b2c.repository.product.ProductRepository;
import com.mans.ecommerce.b2c.utills.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CartTestUtill
{

    private boolean active;

    private final String errorMessageTemplate = "%s product info with sku=%s \n variationId=%s \n qty=%s \n amount=%s";

    private ProductLockingValidator lockingValidator;

    private final String BASE_URL = "/carts/{cartId}";

    private WebTestClient webTestClient;

    public CartTestUtill(WebTestClient webTestClient, ProductRepository productRepository, boolean active)
    {
        lockingValidator = new ProductLockingValidator(productRepository);
        this.webTestClient = webTestClient;
        this.active = active;
    }

    public void callApiAndVerifyPassing(
            String cartId,
            BigDecimal productAmount,
            BigDecimal cartExpectedAmount,
            ProductDto dto,
            Map<String, Integer> infoExpectation,
            boolean productInfo
    )
    {
        callApiAndVerifyPassing(cartId, productAmount, cartExpectedAmount, dto,
                                null, infoExpectation, productInfo, false);
    }

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
                                                                               res);
                         }
                     });
    }

    public void callApiAndVerifyPartialOutOfStock(
            String cartId,
            BigDecimal productAmount,
            BigDecimal cartExpectedAmount,
            ProductDto dto,
            Map<String, Integer> infoExpectation)
    {
        callApiAndVerifyPartialOutOfStock(cartId, productAmount, cartExpectedAmount, dto, null, infoExpectation);
    }

    public void callApiAndVerifyPartialOutOfStock(
            String cartId,
            BigDecimal productAmount,
            BigDecimal cartExpectedAmount,
            ProductDto dto,
            Map<String, Integer> productExpectation,
            Map<String, Integer> resExpectation)
    {
        webTestClient.patch()
                     .uri(BASE_URL, cartId)
                     .body(Mono.just(dto), ProductDto.class)
                     .accept(Global.JSON)
                     .exchange()
                     .expectStatus().isEqualTo(HttpStatus.PARTIAL_CONTENT)
                     .expectBody(PartialOutOfStockException.class)
                     .consumeWith(response -> {
                         PartialOutOfStockException ex = response.getResponseBody();
                         Cart cart = ex.getCart();

                         assertThat(ex.getMessage(), equalToIgnoringCase("This product has only 5 of these available"));

                         assertCartActivationStatus(cart);
                         assertTrue("cart total not the expected",
                                    cart.getMoney().getAmount().equals(cartExpectedAmount));

                         verifyCartProductInfo(cart, resExpectation, productAmount, true);

                         if (active)
                         {
                             lockingValidator.productQuantityAndHasReservation(productExpectation,
                                                                               resExpectation,
                                                                               cartId,
                                                                               true);
                         }
                     });
    }

    public void callApiAndVerifyOutOfStock(
            String cartId,
            ProductDto dto)
    {
        webTestClient.patch()
                     .uri(BASE_URL, cartId)
                     .body(Mono.just(dto), ProductDto.class)
                     .accept(Global.JSON)
                     .exchange()
                     .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
                     .expectBody(OutOfStockException.class)
                     .consumeWith(response -> {
                         OutOfStockException out = response.getResponseBody();

                         assertThat(out.getMessage(), equalToIgnoringCase("product is Out Of Stock"));
                     });
    }

    private void verifyCartProductInfo(
            Cart cart,
            Map<String, Integer> expectation,
            BigDecimal productAmount,
            boolean addOrUpdate)
    {
        expectation.forEach((k, v) -> {
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

    public ProductDto getProductDto(String sku, String cartAction, int lock)
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
