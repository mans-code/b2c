package com.mans.ecommerce.b2c.controller.customer;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.financial.Order;
import com.mans.ecommerce.b2c.domain.exception.ConflictException;
import com.mans.ecommerce.b2c.domain.exception.PaymentFailedException;
import com.mans.ecommerce.b2c.domain.exception.UncompletedCheckoutException;
import com.mans.ecommerce.b2c.domain.logic.CartLogic;
import com.mans.ecommerce.b2c.service.*;
import com.mans.ecommerce.b2c.utill.response.CheckoutResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/checkout/{cartId}")
public class CheckoutController
{

    private final String SUCCEEDED = "succeeded";

    private ProductService productService;

    private CartService cartService;

    private CartLogic cartLogic;

    private CheckoutService checkoutService;

    private StripeService stripeService;

    private String stripePublicKey;

    CheckoutController(
            ProductService productService,
            com.mans.ecommerce.b2c.service.CartService cartService,
            CheckoutService checkoutService,
            CartLogic cartLogic,
            StripeService stripeService,
            @Value("${app.stripe.public.key}") String stripePublicKey)
    {
        this.productService = productService;
        this.cartService = cartService;
        this.checkoutService = checkoutService;
        this.cartLogic = cartLogic;
        this.stripeService = stripeService;
        this.stripePublicKey = stripePublicKey;
    }

    @GetMapping("/")
    public CheckoutResponse lock(@PathVariable("cartId") @NotBlank String cartId)
    {
        Cart cart = cartService.findById(cartId);
        if (cart.isActive())
        {
            throw new ConflictException("cart already locked");
        }
        Cart savedCart = cartService.activateAndSave(cart);
        CheckoutResponse res = new CheckoutResponse(savedCart, stripePublicKey);
        List<ProductLockInfo> lockedProduct = checkoutService.lock(cart);
        checkForFailedLocking(lockedProduct, res);
        return res;
    }

    @PostMapping("/complete")
    public Order complete(
            @PathVariable("cartId") @NotBlank String cartId,
            @RequestParam @NotBlank String token)
            throws StripeException
    {
        Cart cart = cartService.findById(cartId);
        double amount = cart.getMoney().getAmount().doubleValue();
        String currency = cart.getMoney().getCurrency().getCurrencyCode();

        Charge charge = stripeService.chargeNewCard(token, amount, currency);
        boolean succeeded = charge.getStatus().equals(SUCCEEDED);

        if (!succeeded)
        {
            throw new PaymentFailedException(charge.getFailureMessage());
        }

        return createOrder(cart);
    }

    @PostMapping("/leaving")
    public void unlock(@PathVariable("cartId") @NotBlank String cartId)
    {
        Cart cart = cartService.findById(cartId);
        if (!cart.isActive())
        {
            return;
        }
        checkoutService.unlock(cart);
    }

    private Order createOrder(Cart cart)
    {
        return new Order();//TODO
    }

    private void checkForFailedLocking(List<ProductLockInfo> lockedProduct, CheckoutResponse res)
    {
        List<ProductLockInfo> failed = lockedProduct
                                               .stream()
                                               .filter(product -> product.getLockedQuentity()
                                                                          != product.getRequestedQuentity())
                                               .collect(Collectors.toList());

        if (!failed.isEmpty())
        {
            throw new UncompletedCheckoutException(res, failed);
        }
    }

}
