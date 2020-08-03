package com.mans.ecommerce.b2c.controller.customer;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.mans.ecommerce.b2c.controller.utills.dto.ProductDto;
import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import com.mans.ecommerce.b2c.domain.enums.CartAction;
import com.mans.ecommerce.b2c.domain.exception.OutOfStockException;
import com.mans.ecommerce.b2c.domain.exception.PartialOutOfStockException;
import com.mans.ecommerce.b2c.domain.logic.CartLogic;
import com.mans.ecommerce.b2c.service.CheckoutService;
import com.mans.ecommerce.b2c.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/carts/{cartId}")
public class CartController
{

    private com.mans.ecommerce.b2c.service.CartService cartService;

    private ProductService productService;

    private CartLogic cartLogic;

    private CheckoutService checkoutService;

    private final String CART = "cart";

    private final int ZERO = 0;

    CartController(
            com.mans.ecommerce.b2c.service.CartService cartService,
            ProductService productService,
            CartLogic cartLogic,
            CheckoutService checkoutService)
    {
        this.cartService = cartService;
        this.productService = productService;
        this.cartLogic = cartLogic;
        this.checkoutService = checkoutService;
    }

    @GetMapping("/")
    public Cart getCart(@PathVariable("cartId") @NotBlank String cartId)
    {
        return cartService.findById(cartId);
    }

    @PatchMapping("/")
    public Cart add(@PathVariable("cartId") @NotBlank String cartId, @RequestBody @Valid ProductDto dto)
    {
        Cart cart = cartService.findById(cartId);
        CartAction action = dto.getCartAction();

        switch (action)
        {
        case UPDATE:
            return updateProductInCart(cart, dto);
        case DELETE:
            return removerProductInCart(cart, dto);
        case RESET:
            return reset(cart, dto);
        default:
            return addProductToCart(cart, dto);
        }
    }

    public Cart reset(Cart cart, ProductDto dto)
    {

        if (cart.isActive())
        {
            checkoutService.unlock(cart);
        }

        cartLogic.removeAllProducts(cart);
        return cartService.save(cart);
    }

    private Cart removerProductInCart(Cart cart, ProductDto dto)
    {
        ProductInfo productInfo = cartLogic.removeProduct(cart, dto);

        if (cart.isActive())
        {
            checkoutService.unlock(cart, productInfo);
        }

        return cartService.save(cart);
    }

    private Cart addProductToCart(Cart cart, ProductDto dto)
    {
        ProductInfo productInfo = productService.getProductInfo(dto);
        int availableQuantity = productInfo.getQuantity();
        int requestedQuantity = dto.getQuantity();
        boolean partialOutOfStock = availableQuantity < requestedQuantity;

        if (availableQuantity == 0)
        {
            throw new OutOfStockException();
        }

        ProductInfo cartProduct = cartLogic.addProduct(cart, productInfo);
        Cart savedCart = cartService.save(cart);
        int quantityToLock = partialOutOfStock ? availableQuantity : requestedQuantity;

        lockIfNeeded(savedCart, cartProduct, quantityToLock);

        if (partialOutOfStock)
        {
            throw new PartialOutOfStockException(savedCart, availableQuantity);
        }

        return savedCart;
    }

    private void lockIfNeeded(Cart cart, ProductInfo cartProduct, int quantityToLock)
    {
        if (!cart.isActive())
        {
            return;
        }
        int locked = checkoutService.partialLock(cart, cartProduct, quantityToLock);
        if (locked < quantityToLock)
        {
            throw new PartialOutOfStockException(cart, locked);
        }
    }

    private Cart updateProductInCart(Cart cart, ProductDto dto)
    {
        if (dto.getQuantity() == ZERO)
        {
            return removerProductInCart(cart, dto);
        }

        ProductInfo cartProduct = cartLogic.getProduct(cart, dto);
        int newQuantity = dto.getQuantity();
        int difference = getQuantityDifference(dto, cartProduct);
        int absDifference = Math.abs(difference);

        if (difference == ZERO)
        {
            return cart;
        }
        else if (difference < ZERO)
        {
            return reduceQuantity(cart, cartProduct, newQuantity, absDifference);
        }
        else
        {
            dto.setQuantity(absDifference);
            return addProductToCart(cart, dto);
        }
    }

    private Cart reduceQuantity(Cart cart, ProductInfo cartProduct, int newQuantity, int deductedQuantity)
    {
        cartLogic.deductMoneyAndQuantity(cart, cartProduct, deductedQuantity);
        checkoutService.unlock(cart, cartProduct, deductedQuantity);
        return cartService.save(cart);
    }

    private int getQuantityDifference(ProductDto dto, ProductInfo cartProduct)
    {
        int oldQuantity = cartProduct.getQuantity();
        int newQuantity = dto.getQuantity();
        int difference = oldQuantity - newQuantity;

        return difference;
    }

}
