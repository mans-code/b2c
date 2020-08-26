package com.mans.ecommerce.b2c.e2e;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.ImmutableMap;
import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.product.Product;
import com.mans.ecommerce.b2c.domain.entity.product.subEntity.Reservation;
import com.mans.ecommerce.b2c.domain.exception.UncompletedCheckoutException;
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
import reactor.test.StepVerifier;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "200000")
public class CheckoutControllerIT
{

    private final String BASE_URL = "/checkout/{cartId}";

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WebTestClient webTestClient;

    @Value("${app.stripe.public.key}")
    private String stripePublicKey;

    @Test
    public void checkout_pass()
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

                         productLockedAndHasReservation(productExpectation, resExpectation, cartId);
                     });
    }

    @Test
    public void checkout_pass_UncompletedCheckout()
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

                         productLockedAndHasReservation(productExpectation, resExpectation, cartId);
                     });
    }

    private void productLockedAndHasReservation(
            Map<String, Integer> productExpectation,
            Map<String, Integer> resExpectation,
            String resId)
    {
        productExpectation.forEach((sku, qty) -> {
            StepVerifier.create(productRepository.getBySku(sku))
                        .consumeNextWith(product -> {
                            int actualProductQuantity = getQuantity(product, sku);
                            int expectedResQuantity = resExpectation.get(sku);

                            assertEquals("Not the expected locked quantity", actualProductQuantity, qty.intValue());
                            verifyReservation(product.getReservations(), resId, sku, expectedResQuantity);

                        }).verifyComplete();
        });
    }

    private void verifyReservation(
            List<Reservation> reservations,
            String resId,
            String vacationId,
            int expectedQuantity)
    {
        Optional<Reservation> opt = reservations.stream()
                                                .filter(res -> res.getId().equalsIgnoreCase(resId)
                                                                       && res.getVariationId()
                                                                             .equalsIgnoreCase(vacationId)
                                                                       && res.getQuantity() == expectedQuantity)
                                                .findFirst();
        assertTrue("reservation does not exist", opt.isPresent());
    }

    private int getQuantity(Product product, String variationId)
    {
        return product.getVariationsDetails()
                      .get(variationId)
                      .getAvailability()
                      .getQuantity();
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

}
