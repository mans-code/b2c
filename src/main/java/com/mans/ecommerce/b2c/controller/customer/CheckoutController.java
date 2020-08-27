package com.mans.ecommerce.b2c.controller.customer;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import com.mans.ecommerce.b2c.controller.utill.dto.CheckoutDto;
import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.financial.Order;
import com.mans.ecommerce.b2c.domain.entity.financial.subEntity.Financial;
import com.mans.ecommerce.b2c.domain.exception.PaymentFailedException;
import com.mans.ecommerce.b2c.domain.exception.UnableToUpdateCartAfterUncompletedCheckout;
import com.mans.ecommerce.b2c.domain.exception.UncompletedCheckoutException;
import com.mans.ecommerce.b2c.domain.logic.CartLogic;
import com.mans.ecommerce.b2c.server.eventListener.entity.OrderEvent;
import com.mans.ecommerce.b2c.service.CartService;
import com.mans.ecommerce.b2c.service.CheckoutService;
import com.mans.ecommerce.b2c.service.StripeService;
import com.mans.ecommerce.b2c.utill.LockError;
import com.mans.ecommerce.b2c.utill.response.CheckoutResponse;
import com.stripe.model.Charge;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@RestController
@RequestMapping("/checkout/{cartId}")
public class CheckoutController
{

    private final String SUCCEEDED = "succeeded";

    private CartService cartService;

    private CartLogic cartLogic;

    private CheckoutService checkoutService;

    private StripeService stripeService;

    private String stripePublicKey;

    private ApplicationEventPublisher publisher;

    CheckoutController(
            CartService cartService,
            CheckoutService checkoutService,
            CartLogic cartLogic,
            StripeService stripeService,
            ApplicationEventPublisher publisher,
            @Value("${app.stripe.public.key}") String stripePublicKey)
    {
        this.cartService = cartService;
        this.checkoutService = checkoutService;
        this.cartLogic = cartLogic;
        this.stripeService = stripeService;
        this.stripePublicKey = stripePublicKey;
        this.publisher = publisher;
    }

    @PostMapping("/")
    public Mono<CheckoutResponse> lock(@PathVariable("cartId") @NotNull ObjectId cartId)
    {
        Mono<Tuple2<Cart, List<LockError>>> tuple2Mono = checkoutService.lock(cartId);

        return tuple2Mono.flatMap(tuple2 -> {
            Cart cart = tuple2.getT1();
            List<LockError> lockErrors = tuple2.getT2();
            CheckoutResponse res = new CheckoutResponse(stripePublicKey, cart);

            if (lockErrors.isEmpty())
            {
                return Mono.just(res);
            }
            return updateAfterUncompletedCheckout(cart, res, lockErrors);
        });
    }

    @PostMapping("/complete")
    public Mono<Financial> complete(
            @PathVariable("cartId") @NotNull ObjectId cartId,
            @RequestParam @Valid CheckoutDto checkoutDto)
    {
        Mono<Cart> cartMono = cartService.findById(cartId);

        Mono<Charge> chargeMono = getChargeMono(checkoutDto, cartMono);

        Mono<Tuple2<Cart, Charge>> completeMono = Mono.zip(cartMono, chargeMono);

        return completeOrder(checkoutDto, completeMono);
    }

    @PostMapping("/leaving")
    public Mono<Cart> unlock(@PathVariable("cartId") @NotNull ObjectId cartId)
    {
        Mono<Cart> cartMono = cartService.findAndUnlock(cartId);
        return cartMono.doOnSuccess(cart -> {
                checkoutService.unlock(cart, cart.getProductInfos()).subscribe();
            });
    }

    private Mono<? extends CheckoutResponse> updateAfterUncompletedCheckout(
            Cart cart,
            CheckoutResponse res,
            List<LockError> lockErrors)
    {
        cartLogic.update(cart, lockErrors);
        return cartService.update(cart)
                          .doOnError(err -> Mono.error(
                                  new UnableToUpdateCartAfterUncompletedCheckout(cart, lockErrors)))
                          .flatMap(updatedCart -> Mono.error(
                                  new UncompletedCheckoutException(res, lockErrors)));
    }

    private Mono<Charge> getChargeMono(
            @RequestParam @Valid CheckoutDto checkoutDto,
            Mono<Cart> cartMono)
    {
        return cartMono.flatMap(cart -> {
            double amount = calculateTotalAmount(cart, checkoutDto);
            String token = checkoutDto.getToken();
            String currency = cart.getMoney()
                                  .getCurrency()
                                  .getCurrencyCode();
            return Mono.fromCallable(() -> stripeService.charge(token, amount, currency));
        });
    }

    private Mono<Financial> completeOrder(
            @RequestParam @Valid CheckoutDto checkoutDto,
            Mono<Tuple2<Cart, Charge>> completeMono)
    {
        return completeMono.flatMap(tuple2 -> {
            Cart cart = tuple2.getT1();
            Charge charge = tuple2.getT2();
            boolean succeeded = charge.getStatus().equals(SUCCEEDED);

            if (!succeeded)
            {
                Mono.error(new PaymentFailedException(charge.getFailureMessage()));
            }
            Order order = new Order(cart, checkoutDto.getAddress(), charge);
            publisher.publishEvent(new OrderEvent(order));
            return Mono.just(order.getFinancial());
        });
    }

    private double calculateTotalAmount(Cart cart, CheckoutDto checkoutDto)
    {
        return cart.getMoney()
                   .getAmount()
                   .doubleValue();
    }
}
