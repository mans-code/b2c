package com.mans.ecommerce.b2c.controller.customer;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

import com.mans.ecommerce.b2c.controller.utills.dto.AddProductDto;
import com.mans.ecommerce.b2c.controller.utills.dto.ProductDto;
import com.mans.ecommerce.b2c.controller.utills.dto.UpdateProductDto;
import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import com.mans.ecommerce.b2c.domain.exception.OutOfStockException;
import com.mans.ecommerce.b2c.domain.exception.PartialOutOfStockException;
import com.mans.ecommerce.b2c.domain.logic.CartLogic;
import com.mans.ecommerce.b2c.service.CartService;
import com.mans.ecommerce.b2c.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart/{cartId}")
public class CartController
{

    private CartService cartService;

    private ProductService productService;

    private CartLogic cartLogic;

    private final String CART = "cart";

    private final int ZERO = 0;

    CartController(
            CartService cartService,
            ProductService productService,
            CartLogic cartLogic)
    {
        this.cartService = cartService;
        this.productService = productService;
        this.cartLogic = cartLogic;
    }

    @GetMapping("/")
    public Cart getCart(@PathVariable("cartId") @NotBlank String cartId)
    {
        return cartService.findById(cartId);
    }

    @PatchMapping("/add")
    public Cart add(@PathVariable("cartId") @NotBlank String cartId, @RequestBody @Valid AddProductDto dto)
    {
        Cart cart = cartService.findById(cartId);
        return addProductToCart(cart, dto);
    }

    @PatchMapping("/update")
    public Cart update(@PathVariable("cartId") @NotBlank String cartId, @RequestBody @Valid UpdateProductDto dto)
    {
        Cart cart = cartService.findById(cartId);
        return updateProductInCart(cart, dto);
    }

    @PutMapping("/reset")
    public Cart reset(@PathVariable("cartId") @NotBlank String cartId)
    {
        Cart cart = cartService.findById(cartId);
        List<ProductInfo> productInfoList = cartLogic.removeAllProducts(cart);
        productService.unlock(cart, productInfoList);
        return cartService.save(cart);
    }

    private Cart removerProductInCart(Cart cart, ProductDto dto)
    {
        ProductInfo productInfo = cartLogic.removeProduct(cart, dto);
        productService.unlock(cart, productInfo);
        return cartService.save(cart);
    }

    private Cart addProductToCart(Cart cart, ProductDto dto)
    {
        ProductInfo productInfo = productService.getProductInfo(dto);
        int availableQuantity = productInfo.getQuantity();
        int requestedQuantity = dto.getQuantity();

        if (availableQuantity == 0)
        {
            throw new OutOfStockException();
        }
        else if (availableQuantity < requestedQuantity)
        {
            productInfo.setQuantity(availableQuantity);
            Cart savedCart = cartService.save(cart);
            throw new PartialOutOfStockException(savedCart, availableQuantity);
        }
        else
        {
            cartLogic.addProduct(cart, productInfo);
            return cartService.save(cart);
        }

    }

    private Cart updateProductInCart(Cart cart, ProductDto dto)
    {
        if (dto.getQuantity() == ZERO)
        {
            return removerProductInCart(cart, dto);
        }

        ProductInfo cartProduct = cartLogic.getProduct(cart, dto);
        int difference = getQuantityDifference(dto, cartProduct);
        int absDifference = Math.abs(difference);

        if (difference == ZERO)
        {
            return cart;
        }
        else if (difference < ZERO)
        {
            return reduceQuantity(cart, cartProduct, absDifference);
        }
        else
        {
            dto.setQuantity(absDifference);
            return addProductToCart(cart, dto);
        }
    }

    private Cart reduceQuantity(Cart cart, ProductInfo cartProduct, int deductedQuantity)
    {
        cartLogic.deductMoneyAndQuantity(cart, cartProduct, deductedQuantity);
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
