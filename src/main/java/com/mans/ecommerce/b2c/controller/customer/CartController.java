package com.mans.ecommerce.b2c.controller.customer;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

import com.mans.ecommerce.b2c.controller.utills.dto.ProductInfoDto;
import com.mans.ecommerce.b2c.controller.utills.entity.CartEntityUtill;
import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import com.mans.ecommerce.b2c.domain.exception.OutOfStock;
import com.mans.ecommerce.b2c.domain.exception.PartialOutOfStock;
import com.mans.ecommerce.b2c.domain.exception.ResourceNotFoundException;
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

    private CartEntityUtill cartEntityUtill;

    CartController(
            CartService cartService,
            ProductService productService,
            CartEntityUtill cartEntityUtill)
    {
        this.cartService = cartService;
        this.productService = productService;
        this.cartEntityUtill = cartEntityUtill;
    }

    @GetMapping("/")
    public Cart getCart(@PathVariable("cartId") @NotBlank String cartId)
    {
        return cartService.findByIdOrElseThrow(cartId);
    }

    @PostMapping("/")
    public Cart add(@PathVariable("cartId") @NotBlank String cartId, @RequestBody @Valid ProductInfoDto productInfoDto)
    {
        Cart cart = cartService.findByIdOrElseThrow(cartId);
        ProductInfo productInfo = productService.getProductInfoOrElseThrow(productInfoDto);

        if(productInfo.getQuantity() == 0)
        {
            throw new OutOfStock("product is Out Of Stock ");
        }

        if(productInfo.getQuantity() != productInfoDto.getQuantity())
        {
            cartService.save(cart);
            throw new PartialOutOfStock("", cart);
        }
        return cartService.save(cart);
    }

    @DeleteMapping("/")
    public Cart delete(
            @PathVariable("cartId") @NotBlank String cartId,
            @RequestBody @Valid ProductInfoDto productInfoDto)
    {
        Cart cart = cartService.findByIdOrElseThrow(cartId);
        Optional<ProductInfo> productInfo = cartEntityUtill.removeProductInfo(cart, productInfoDto);

        if (!productInfo.isPresent())
        {
            throw new ResourceNotFoundException("couldn't find a product with the given ID in cart");
        }
        productService.unLock(cart, productInfo.get());
        return cartService.save(cart);
    }

    @DeleteMapping("/deleteAll")
    public Cart deleteAll(@PathVariable("cartId") @NotBlank String cartId)
    {
        Cart cart = cartService.findByIdOrElseThrow(cartId);
        List<ProductInfo> productInfoList = cartEntityUtill.removeAllProducts(cart);
        productService.unlock(cart, productInfoList);
        return cartService.save(cart);
    }

}
