package com.mans.ecommerce.b2c.domain.logic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mans.ecommerce.b2c.controller.utills.dto.ProductDto;
import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.Money;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import com.mans.ecommerce.b2c.domain.enums.Currency;
import com.mans.ecommerce.b2c.domain.exception.ResourceNotFoundException;
import com.mans.ecommerce.b2c.server.eventListener.entity.AddProductToCartEvent;
import com.mans.ecommerce.b2c.service.CartService;
import com.mans.ecommerce.b2c.utill.LockError;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class CartLogic
{

    @Getter
    private final String NOT_FOUND = "couldn't find a product with the ID = %s in cart";

    private int validityInMinutes;

    private ApplicationEventPublisher publisher;

    private CartService cartService;

    CartLogic(
            @Value("${app.cart.expiration}") int validityInMinutes,
            CartService cartService,
            ApplicationEventPublisher publisher)
    {
        this.validityInMinutes = validityInMinutes;
        this.cartService = cartService;
        this.publisher = publisher;
    }

    public ProductInfo addProduct(Cart cart, ProductInfo productInfo)
    {
        publisher.publishEvent(new AddProductToCartEvent(cart.getIdObj(), productInfo));
        Optional<ProductInfo> cartProductOpt = getProduct(cart, productInfo.getSku(), productInfo.getVariationId());
        int requestedQuantity = productInfo.getQuantity();
        ProductInfo cartProduct;

        if (cartProductOpt.isPresent())
        {
            cartProduct = cartProductOpt.get();
            addQuantityToProduct(cartProduct, requestedQuantity);
        }
        else
        {
            cartProduct = productInfo;
            List<ProductInfo> productInfos = cart.getProductInfos();
            productInfos.add(productInfo);
        }

        addMoneyAndQuantity(cart, productInfo, requestedQuantity);

        return cartProduct;
    }

    public ProductInfo removeProduct(Cart cart, ProductDto dto)
    {
        ProductInfo productInfo = getProduct(cart, dto);
        return removeProduct(cart, productInfo);
    }

    public List<ProductInfo> removeAllProducts(Cart cart)
    {
        List<ProductInfo> productInfos = resetProductInfoList(cart);
        resetMoneyAndQuantity(cart);
        cart.setActive(false);
        return productInfos;
    }

    public void deductMoneyAndQuantity(Cart cart, ProductInfo productInfo, int quantity)
    {
        Money moneyToDeduct = getMoney(productInfo, quantity);
        deductMoney(cart, moneyToDeduct);
        deductQuantity(cart, quantity);
        productInfo.setQuantity(quantity);
    }

    public ProductInfo getProduct(Cart cart, ProductDto dto)
    {
        Optional<ProductInfo> optional = getProduct(cart, dto.getSku(), dto.getVariationId());

        if (!optional.isPresent())
        {
            throw new ResourceNotFoundException(NOT_FOUND);
        }
        return optional.get();
    }

    private void addQuantityToProduct(ProductInfo cartProduct, int requestedQuantity)
    {
        int prevQuantity = cartProduct.getQuantity();
        int newQuantity = requestedQuantity + prevQuantity;
        cartProduct.setQuantity(newQuantity);
    }

    private List<ProductInfo> resetProductInfoList(Cart cart)
    {
        List<ProductInfo> productInfos = cart.getProductInfos();
        cart.setProductInfos(new ArrayList<>());
        return productInfos;
    }

    private void addMoneyAndQuantity(Cart cart, ProductInfo productInfo, int quantity)
    {
        Money amountToAdd = getMoney(productInfo, quantity);
        addMoney(cart, amountToAdd);
        addQuantity(cart, quantity);
    }

    private void addMoney(Cart cart, Money amountToAdd)
    {
        Money oldCartCost = cart.getMoney();
        Money newCartCost = sum(amountToAdd, oldCartCost);
        cart.setMoney(newCartCost);
    }

    private void addQuantity(Cart cart, int quantity)
    {
        int oldQuantity = cart.getTotalQuantity();
        int newQuantity = oldQuantity + quantity;
        cart.setTotalQuantity(newQuantity);
    }

    private void deductMoney(Cart cart, Money deduct)
    {
        Money newCartMoney = subtract(deduct, cart);
        cart.setMoney(newCartMoney);
    }

    private void deductQuantity(Cart cart, int deduct)
    {
        int oldCartQuantity = cart.getTotalQuantity();
        int newCartQuantity = oldCartQuantity - deduct;
        cart.setTotalQuantity(newCartQuantity);
    }

    private void resetMoneyAndQuantity(Cart cart)
    {
        resetMoney(cart);
        resetQuantity(cart);
    }

    private void resetQuantity(Cart cart)
    {
        cart.setTotalQuantity(0);
    }

    private void resetMoney(Cart cart)
    {
        Currency currency = cart.getMoney().getCurrency();
        Money money = new Money(new BigDecimal("0.00"), currency);
        cart.setMoney(money);
    }

    private Money sum(Money product, Money cart)
    {
        if (product.getCurrency() != cart.getCurrency())
        {
            throw new UnsupportedOperationException();
        }

        BigDecimal firstAmount = product.getAmount();
        BigDecimal secondAmount = cart.getAmount();
        BigDecimal sumAmount = firstAmount.add(secondAmount);
        return new Money(sumAmount, cart.getCurrency());
    }

    private Money subtract(Money product, Cart cart)
    {
        Money cartMoney = cart.getMoney();
        BigDecimal cartAmount = cartMoney.getAmount();
        BigDecimal productAmount = product.getAmount();
        BigDecimal subtractAmount = cartAmount.subtract(productAmount);
        return new Money(subtractAmount, cartMoney.getCurrency());
    }

    private Money getMoney(ProductInfo productInfo, int quantity)
    {
        Money productMoney = productInfo.getPrice().getMoney();
        BigDecimal productAmount = productMoney.getAmount();
        BigDecimal quantityBD = BigDecimal.valueOf(quantity);
        BigDecimal totalCost = productAmount.multiply(quantityBD);
        return new Money(totalCost, productMoney.getCurrency());
    }

    private Optional<ProductInfo> getProduct(Cart cart, String sku, String variationId)
    {
        return cart
                       .getProductInfos()
                       .stream()
                       .filter(product ->
                                       product.getSku().equals(sku) &&
                                               product.getVariationId().equals(variationId))
                       .findFirst();
    }

    private ProductInfo removeProduct(Cart cart, ProductInfo productInfo)
    {
        int quantity = productInfo.getQuantity();
        deductMoneyAndQuantity(cart, productInfo, quantity);
        cart.getProductInfos().remove(productInfo);
        return productInfo;
    }

    public void update(Cart cart, List<LockError> lockErrors)
    {
        lockErrors.forEach(lockError -> {
            int lockedQyt = lockError.getLockedQuantity();
            ProductInfo cartProduct = getProduct(cart, lockError.getSku(), lockError.getVariationId()).get();

            if (lockedQyt == 0)
            {
                removeProduct(cart, cartProduct);
            }
            else
            {
                int toDeduct = cartProduct.getQuantity() - lockError.getLockedQuantity();
                deductMoneyAndQuantity(cart, cartProduct, toDeduct);
            }
        });
    }
}
