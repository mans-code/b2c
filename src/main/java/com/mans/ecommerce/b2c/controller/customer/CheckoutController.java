package com.mans.ecommerce.b2c.controller.customer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.customer.subEntity.Address;
import com.mans.ecommerce.b2c.domain.entity.financial.Order;
import com.mans.ecommerce.b2c.domain.entity.financial.subEntity.Financial;
import com.mans.ecommerce.b2c.domain.exception.ConflictException;
import com.mans.ecommerce.b2c.domain.exception.PaymentFailedException;
import com.mans.ecommerce.b2c.domain.exception.UncompletedCheckoutException;
import com.mans.ecommerce.b2c.domain.logic.CartLogic;
import com.mans.ecommerce.b2c.service.*;
import com.mans.ecommerce.b2c.utill.ProductLockErrorInfo;
import com.mans.ecommerce.b2c.utill.response.CheckoutResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

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

    private String stripePublicKey;

    CheckoutController(
            ProductService productService,
            CartService cartService,
            CheckoutService checkoutService,
            CartLogic cartLogic,
            StripeService stripeService,
            CustomerService customerService,
            OrderService orderService,
            @Value("${app.stripe.public.key}") String stripePublicKey)
    {
        this.productService = productService;
        this.cartService = cartService;
        this.checkoutService = checkoutService;
        this.cartLogic = cartLogic;
        this.stripeService = stripeService;
        this.stripePublicKey = stripePublicKey;
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @PostMapping("/")
    public CheckoutResponse lock(@PathVariable("cartId") @NotNull ObjectId cartId)
    {
        Cart cart = cartService.findById(cartId);
        if (cart.isActive())
        {
            throw new ConflictException("cart already locked");
        }
        List<ProductLockErrorInfo> lockedProductError = checkoutService.lock(cart);
        CheckoutResponse res = new CheckoutResponse(cart, stripePublicKey);
        cartService.activateAndSave(cart);

        if (!lockedProductError.isEmpty())
        {
            throw new UncompletedCheckoutException(res, lockedProductError);
        }
        return res;
    }

    @PostMapping("/complete")
    public Financial complete(
            @PathVariable("cartId") @NotNull ObjectId cartId,
            @RequestParam @NotBlank String shippingId,
            @RequestParam @NotBlank String token)
            throws StripeException
    {
        Cart cart = cartService.findById(cartId);
        Address address = customerService.getDefaultShippingAddress(cartId);
        double totalAmount = calculateTotalAmount(cart, address, shippingId);
        String currency = cart.getMoney().getCurrency().getCurrencyCode();

        Charge charge = stripeService.chargeNewCard(token, totalAmount, currency);
        boolean succeeded = charge.getStatus().equals(SUCCEEDED);

        if (!succeeded)
        {
            throw new PaymentFailedException(charge.getFailureMessage());
        }

        Order order = new Order(cart, address, charge);
        orderService.save(order);
        return order.getFinancial();
    }

    @PostMapping("/leaving")
    public void unlock(@PathVariable("cartId") @NotNull ObjectId cartId)
    {
        Cart cart = cartService.findById(cartId);
        if (cart.isActive())
        {
            checkoutService.unlock(cartId, cart.getProductInfos());
        }
    }

    private double calculateTotalAmount(//TODO
                                        Cart cart,
                                        Address address,
                                        String shippingId)
    {
        return cart.getMoney().getAmount().doubleValue();
    }
}
