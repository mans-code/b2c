package com.mans.ecommerce.b2c.domain.logic;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.IntStream;

import com.mans.ecommerce.b2c.controller.utills.dto.ProductDto;
import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.Money;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import com.mans.ecommerce.b2c.domain.enums.Currency;
import com.mans.ecommerce.b2c.domain.exception.ResourceNotFoundException;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class CartLogic
{

    @Getter
    private final String NOT_FOUND = "couldn't find a product with the ID = %s in cart";

    public void addProduct(Cart cart, ProductInfo productInfo)
    {
        //TODO shouldn't throw
        List<ProductInfo> productInfos = cart.getProductInfos();
        int quantity = productInfo.getQuantity();

        addMoneyAndQuantity(cart, productInfo, quantity);
        productInfos.add(productInfo);
    }

    public ProductInfo removeProductInfo(
            Cart cart,
            ProductDto dto)
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

    public ProductInfo updateProductQuantity(Cart cart, ProductDto dto)
    {
        ProductInfo productInfo = getProduct(cart, dto);
        int oldQuantity = productInfo.getQuantity();
        int newQuantity = dto.getQuantity();
        int absDifference = Math.abs(oldQuantity - newQuantity);

        if (oldQuantity < newQuantity)
        {
            addMoneyAndQuantity(cart, productInfo, absDifference);
        }
        else
        {
            deductMoneyAndQuantity(cart, productInfo, absDifference);
        }

        productInfo.setQuantity(newQuantity);
        return productInfo;
    }

    public ProductInfo getProduct(Cart cart, ProductDto dto)
    {
        String desireSku = constructSku(dto);
        List<ProductInfo> productInfoList = cart.getProductInfos();
        OptionalInt index = getIndex(cart, dto);
        if(!index.isPresent())
        {
            throw new ResourceNotFoundException(NOT_FOUND);
        }
        return productInfoList.get(index.getAsInt());
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

    private void deductMoneyAndQuantity(Cart cart, ProductInfo productInfo, int quantity)
    {
        Money moneyToDeduct = getMoney(productInfo, quantity);
        deductMoney(cart, moneyToDeduct);
        deductQuantity(cart, quantity);
    }

    private void deductMoney(Cart cart, Money deduct)
    {
        if (deduct.getCurrency() != cart.getMoney().getCurrency())
        {
            throw new UnsupportedOperationException();
        }

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

    private OptionalInt getIndex(List<ProductInfo> productInfoList, String desireSku)
    {
        return IntStream
                       .range(0, productInfoList.size())
                       .filter(i -> productInfoList.get(i).getSku().equals(desireSku))
                       .findFirst();
    }



}
