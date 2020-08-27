package com.mans.ecommerce.b2c.e2e;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.ImmutableMap;
import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.exception.ResourceNotFoundException;
import com.mans.ecommerce.b2c.domain.exception.UncompletedCheckoutException;
import com.mans.ecommerce.b2c.e2e.utill.ProductLockingValidator;
import com.mans.ecommerce.b2c.repository.product.ProductRepository;
import com.mans.ecommerce.b2c.utill.LockError;
import com.mans.ecommerce.b2c.utill.response.CheckoutResponse;
import com.mans.ecommerce.b2c.utills.Global;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class CheckoutControllerIT
{

    private final String BASE_URL = "/checkout/{cartId}";

    @Autowired
    private WebTestClient webTestClient;

    @Value("${app.stripe.public.key}")
    private String stripePublicKey;

    private ProductLockingValidator lockingValidator;

    @Test
    public void startCheckout_pass()
    {
        String cartId = "5eaa32339e58d82df4319999";
        Map<String, Integer> productExpectation = passCheckoutProductExpectation();
        Map<String, Integer> resExpectation = passCheckoutResExpectation();

        webTestClient.post()
                     .uri(BASE_URL + "/", cartId)
                     .accept(Global.JSON)
                     .exchange()
                     .expectStatus().is2xxSuccessful()
                     .expectBody(CheckoutResponse.class)
                     .consumeWith(res -> {
                         Cart expectedCart = res.getResponseBody().getCart();
                         String expectedStripePublicKey = res.getResponseBody().getStripePublicKey();

                         assertThat("no stripe Public Key", expectedStripePublicKey,
                                    equalToIgnoringCase(stripePublicKey));

                         assertTrue(expectedCart.isActive());

                         lockingValidator.productQuantityAndHasReservation(productExpectation,
                                                                           resExpectation,
                                                                           cartId,
                                                                           true);
                     });
    }

    @Test
    public void startCheckout_pass_UncompletedCheckout()
    {
        String cartId = "5eaa32339e58d82df4319995";
        String sku = "mans-17";

        Map<String, Integer> productExpectation = ImmutableMap.<String, Integer>builder().put(sku, 0).build();
        Map<String, Integer> resExpectation = ImmutableMap.<String, Integer>builder().put(sku, 10).build();

        webTestClient.post()
                     .uri(BASE_URL + "/", cartId)
                     .accept(Global.JSON)
                     .exchange()
                     .expectStatus().isEqualTo(HttpStatus.PARTIAL_CONTENT)
                     .expectBody(UncompletedCheckoutException.class)
                     .consumeWith(res -> {

                         UncompletedCheckoutException expectedException = res.getResponseBody();
                         CheckoutResponse checkoutResponse = expectedException.getCheckoutResponse();
                         Cart expectedCart = checkoutResponse.getCart();
                         List<LockError> lockErrors = expectedException.getUncompleted();
                         String expectedStripePublicKey = checkoutResponse.getStripePublicKey();

                         assertThat("no stripe Public Key", expectedStripePublicKey,
                                    equalToIgnoringCase(stripePublicKey));

                         assertTrue(expectedCart.isActive());

                         verifyLockErrors(lockErrors, resExpectation);
                         lockingValidator.productQuantityAndHasReservation(productExpectation,
                                                                           resExpectation,
                                                                           cartId,
                                                                           true);
                     });
    }

    @Test
    public void startCheckout_fail_activeCart()
    {
        String cartId = "5eaa32339e58d82df43199a9";

        webTestClient.post()
                     .uri(BASE_URL + "/", cartId)
                     .accept(Global.JSON)
                     .exchange()
                     .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST)
                     .expectBody(ResourceNotFoundException.class)
                     .consumeWith(res -> {
                         ResourceNotFoundException ex = res.getResponseBody();

                         assertThat(ex.getMessage(), equalToIgnoringCase("could Not find cart or cart is active"));
                     });
    }

    @Test
    public void leavingCheckout_pass()
    {
        String cartId = "5eaa32339e58d82df43199aa";
        String sku = "mans-41";
        Map<String, Integer> productExpectation = ImmutableMap.<String, Integer>builder().put(sku, 107).build();
        Map<String, Integer> resExpectation = ImmutableMap.<String, Integer>builder().put(sku, 7).build();

        webTestClient.post()
                     .uri(BASE_URL + "/leaving", cartId)
                     .accept(Global.JSON)
                     .exchange()
                     .expectStatus().is2xxSuccessful()
                     .expectBody(Cart.class)
                     .consumeWith(res -> {
                         Cart cart = res.getResponseBody();

                         assertFalse(cart.isActive());
                         lockingValidator.productQuantityAndHasReservation(productExpectation,
                                                                           resExpectation,
                                                                           cartId,
                                                                           false);
                     });
    }

    @Test
    public void leavingCheckout_fail_CartIsNotActive()
    {
        String cartId = "5eaa32339e58d82df43199ae";

        webTestClient.post()
                     .uri(BASE_URL + "/leaving", cartId)
                     .accept(Global.JSON)
                     .exchange()
                     .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST)
                     .expectBody(ResourceNotFoundException.class)
                     .consumeWith(res -> {
                         ResourceNotFoundException ex = res.getResponseBody();

                         assertThat(ex.getMessage(), equalToIgnoringCase("could Not find cart or cart is not active"));
                     });
    }

    private void verifyLockErrors(List<LockError> lockErrors, Map<String, Integer> lockExpectation)
    {
        lockExpectation.forEach((k, v) -> {

            Optional<LockError> opt = lockErrors.stream().filter(lockError -> lockError.getSku().equalsIgnoreCase(k)
                                                                                      && lockError.getVariationId()
                                                                                                  .equalsIgnoreCase(k)
                                                                                      && lockError.getLockedQuantity()
                                                                                                 == v).findFirst();
            assertTrue(opt.isPresent());
        });
    }

    private Map<String, Integer> passCheckoutProductExpectation()
    {
        String firstSku = "mans-5";
        String secondSku = "mans-31";

        int firstProductExpectedQuantity = 964;
        int secondProductExpectedQuantity = 208;

        return ImmutableMap.<String, Integer>builder()
                       .put(firstSku, firstProductExpectedQuantity)
                       .put(secondSku, secondProductExpectedQuantity)
                       .build();
    }

    private Map<String, Integer> passCheckoutResExpectation()
    {
        String firstVariationId = "mans-5";
        String secondVariationId = "mans-31";

        int firstResQuantity = 4;
        int secondResQuantity = 5;

        return ImmutableMap.<String, Integer>builder()
                       .put(firstVariationId, firstResQuantity)
                       .put(secondVariationId, secondResQuantity)
                       .build();
    }

    @Autowired
    public void lockingValidatorInstance(ProductRepository productRepository)
    {
        lockingValidator = new ProductLockingValidator(productRepository);
    }
}
