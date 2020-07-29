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
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CartLogic
{

    @Getter
    private final String NOT_FOUND = "couldn't find a product with the ID = %s in cart";

    @Value("${app.cart.expiration}")
    private long validityInMinutes;

    CartLogic(@Value("${app.cart.expiration}") long validityInMinutes)
    {
        this.validityInMinutes = validityInMinutes;
    }

    public void addProduct(Cart cart, ProductInfo productInfo)
    {
        Optional<ProductInfo> cartProductOpt = getProduct(cart, productInfo.getSku());
        int requestedQuantity = productInfo.getQuantity();

        if (cartProductOpt.isPresent())
        {
            ProductInfo cartProduct = cartProductOpt.get();
            addQuantityToProduct(cartProduct, requestedQuantity);
        }
        else
        {
            List<ProductInfo> productInfos = cart.getProductInfos();
            productInfos.add(productInfo);
        }

        addMoneyAndQuantity(cart, productInfo, requestedQuantity);
    }

    public ProductInfo removeProduct(Cart cart, ProductDto dto)
    {
        ProductInfo productInfo = getProduct(cart, dto);
        int quantity = productInfo.getQuantity();

        deductMoneyAndQuantity(cart, productInfo, quantity);
        cart.getProductInfos().remove(productInfo);
        return productInfo;
    }

    public List<ProductInfo> removeAllProducts(Cart cart)
    {
        List<ProductInfo> productInfos = resetProductInfoList(cart);
        resetMoneyAndQuantity(cart);
        return productInfos;
    }

    public void deductMoneyAndQuantity(Cart cart, ProductInfo productInfo, int quantity)
    {
        Money moneyToDeduct = getMoney(productInfo, quantity);
        deductMoney(cart, moneyToDeduct);
        deductQuantity(cart, quantity);
    }

    public ProductInfo getProduct(Cart cart, ProductDto dto)
    {
        String sku = dto.getSku();
        Optional<ProductInfo> optional = getProduct(cart, sku);

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

    private Optional<ProductInfo> getProduct(Cart cart, String desireSku)
    {
        return cart
                       .getProductInfos()
                       .stream()
                       .filter(product -> product.getSku().equals(desireSku))
                       .findFirst();
    }

}
