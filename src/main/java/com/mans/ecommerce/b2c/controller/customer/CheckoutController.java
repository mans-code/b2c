package com.mans.ecommerce.b2c.controller.customer;

import javax.validation.constraints.NotBlank;
import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import com.mans.ecommerce.b2c.domain.exception.ConflictException;
import com.mans.ecommerce.b2c.service.CartService;
import com.mans.ecommerce.b2c.service.CheckoutService;
import com.mans.ecommerce.b2c.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/checkout/{cartId}")
public class CheckoutController
{
    private ProductService productService;

    private CartService cartService;

    private CheckoutService checkoutService;

    CheckoutController(
            ProductService productService,
            CartService cartService,
            CheckoutService checkoutService)
    {
        this.productService = productService;
        this.cartService = cartService;
        this.checkoutService = checkoutService;
    }

    @GetMapping("/")
    public Cart lock(@PathVariable("cartId") @NotBlank String cartId)
    {
        Cart cart = cartService.findById(cartId);
        if (cart.isActive())
        {
            throw new ConflictException("cart already locked");
        }

        List<ProductInfo> lockedProduct = checkoutService.lock(cart);
        return cartService.save(cart);
    }

    @GetMapping("/leaving")
    public void unlock(@PathVariable("cartId") @NotBlank String cartId)
    {
        Cart cart = cartService.findById(cartId);
        if (!cart.isActive())
        {
            return;
        }
        checkoutService.unlock(cart);
    }

}
