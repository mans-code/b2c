package com.mans.ecommerce.b2c.controller.customer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.customer.subEntity.Address;
import com.mans.ecommerce.b2c.domain.entity.financial.subEntity.Financial;
import com.mans.ecommerce.b2c.domain.exception.UnableToUpdateCartAfterUncompletedCheckout;
import com.mans.ecommerce.b2c.domain.exception.UncompletedCheckoutException;
import com.mans.ecommerce.b2c.domain.logic.CartLogic;
import com.mans.ecommerce.b2c.service.*;
import com.mans.ecommerce.b2c.utill.LockError;
import com.mans.ecommerce.b2c.utill.response.CheckoutResponse;
import com.stripe.exception.StripeException;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@RestController
@RequestMapping("/checkout/{cartId}")
public class CheckoutController
{

    private final String SUCCEEDED = "succeeded";

    private ProductService productService;

    private CartService cartService;

    private CustomerService customerService;

    private CartLogic cartLogic;

    private CheckoutService checkoutService;

    private OrderService orderService;

    private StripeService stripeService;

    CheckoutController(
            ProductService productService,
            CartService cartService,
            CheckoutService checkoutService,
            CartLogic cartLogic,
            StripeService stripeService,
            CustomerService customerService,
            OrderService orderService)
    {
        this.productService = productService;
        this.cartService = cartService;
        this.checkoutService = checkoutService;
        this.cartLogic = cartLogic;
        this.stripeService = stripeService;
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @PostMapping("/")
    public Mono<CheckoutResponse> lock(@PathVariable("cartId") @NotNull ObjectId cartId)
    {

        Mono<Tuple2<Cart, List<LockError>>> tuple2Mono = checkoutService.lock(cartId);

        return tuple2Mono.flatMap(tuple2 -> {
            List<LockError> lockErrors = tuple2.getT2();
            Cart cart = tuple2.getT1();
            CheckoutResponse res = new CheckoutResponse(cart);

            if (!lockErrors.isEmpty())
            {
                return Mono.just(res);
            }

            cartLogic.update(cart, lockErrors);
            return cartService.update(cart)
                              .doOnError(err -> Mono.error(
                                      new UnableToUpdateCartAfterUncompletedCheckout(cart, lockErrors)))
                              .flatMap(updatedCart -> Mono.error(
                                      new UncompletedCheckoutException(updatedCart, lockErrors)));
        });
    }

    @PostMapping("/complete")
    public Financial complete(
            @PathVariable("cartId") @NotNull ObjectId cartId,
            @RequestParam @NotBlank String shippingId,
            @RequestParam @NotBlank String token,
            @RequestParam String shippingAddressId)
            throws StripeException
    {
        Mono<Cart> cart = cartService.findById(cartId);

        //        double totalAmount = calculateTotalAmount(cart, address, shippingId);
        //        String currency = cart.getMoney().getCurrency().getCurrencyCode();
        //
        //        Charge charge = stripeService.chargeNewCard(token, totalAmount, currency);
        //        boolean succeeded = charge.getStatus().equals(SUCCEEDED);
        //
        //        if (!succeeded)
        //        {
        //            throw new PaymentFailedException(charge.getFailureMessage());
        //        }
        //
        //        Order order = new Order(cart, address, charge);
        //        orderService.save(order);
        //        return order.getFinancial();
        return null;
    }

    @PostMapping("/leaving")
    public Mono<Cart> unlock(@PathVariable("cartId") @NotNull ObjectId cartId)
    {
        Mono<Cart> cartMono = cartService.findById(cartId);
        return cartMono.doOnSuccess(cart -> {
            if (cart.isActive())
            {
                checkoutService.unlock(cartId, cart.getProductInfos());
            }
        });
    }

    private double calculateTotalAmount(//TODO
                                        Cart cart,
                                        Address address,
                                        String shippingId)
    {
        return cart.getMoney().getAmount().doubleValue();
    }
}
