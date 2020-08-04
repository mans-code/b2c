package com.mans.ecommerce.b2c.controller.customer;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.customer.subEntity.Address;
import com.mans.ecommerce.b2c.domain.entity.financial.Order;
import com.mans.ecommerce.b2c.domain.entity.financial.subEntity.Financial;
import com.mans.ecommerce.b2c.domain.exception.ConflictException;
import com.mans.ecommerce.b2c.domain.exception.PaymentFailedException;
import com.mans.ecommerce.b2c.domain.exception.UncompletedCheckoutException;
import com.mans.ecommerce.b2c.domain.logic.CartLogic;
import com.mans.ecommerce.b2c.service.*;
import com.mans.ecommerce.b2c.utill.ProductLockInfo;
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
    public Financial complete(
            @PathVariable("cartId") @NotBlank String cartId,
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
    public void unlock(@PathVariable("cartId") @NotBlank String cartId)
    {
        Cart cart = cartService.findById(cartId);
        if (!cart.isActive())
        {
            return;
        }
        checkoutService.unlock(cart);
    }

    @GetMapping("/shipping")
    public List<Address> getShippingAddresses(@PathVariable("cartId") @NotBlank String customerId)
    {
        return customerService.getShippingAddresses(customerId);
    }


    private double calculateTotalAmount(//TODO
            Cart cart,
            Address address,
            String shippingId)
    {
        return cart.getMoney().getAmount().doubleValue();
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
