package com.mans.ecommerce.b2c.e2e.utill;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.mans.ecommerce.b2c.domain.entity.product.Product;
import com.mans.ecommerce.b2c.domain.entity.product.subEntity.Reservation;
import com.mans.ecommerce.b2c.repository.product.ProductRepository;
import reactor.test.StepVerifier;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.awaitility.Awaitility.with;
import static org.junit.Assert.*;

public class ProductLockingValidator
{

    private final ProductRepository productRepository;

    public ProductLockingValidator(ProductRepository productRepository)
    {
        this.productRepository = productRepository;
    }

    public void productQuantityAndHasReservation(
            Map<String, Integer> productExpectation,
            Map<String, Integer> resExpectation,
            String resId,
            boolean res)
    {
        with().pollDelay(250, MILLISECONDS).await().until(() -> true);
        productExpectation.forEach((sku, qty) -> {
            StepVerifier.create(productRepository.getBySku(sku))
                        .consumeNextWith(product -> {
                            int actualProductQuantity = getQuantity(product, sku);
                            int expectedResQuantity = resExpectation.get(sku);

                            assertEquals("Not the expected product quantity", qty.intValue(), actualProductQuantity);
                            verifyReservation(product.getReservations(), resId, sku, expectedResQuantity, res);

                        }).verifyComplete();
        });
    }

    public void verifyReservation(
            List<Reservation> reservations,
            String resId,
            String vacationId,
            int expectedQuantity,
            boolean addRes)
    {
        Optional<Reservation> opt = reservations.stream()
                                                .filter(resv -> resv.getId().equalsIgnoreCase(resId)
                                                                        && resv.getVariationId()
                                                                               .equalsIgnoreCase(vacationId)
                                                                        && resv.getQuantity() == expectedQuantity)
                                                .findFirst();

        if (addRes)
        {
            assertTrue("reservation does not exist", opt.isPresent());
        }
        else
        {
            assertFalse("reservation does exist", opt.isPresent());
        }
    }

    private int getQuantity(Product product, String variationId)
    {
        return product.getVariationsDetails()
                      .get(variationId)
                      .getAvailability()
                      .getQuantity();
    }
}
