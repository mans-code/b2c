package com.mans.ecommerce.b2c.controller.utills.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mans.ecommerce.b2c.controller.utills.dto.ProductInfoDto;
import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.Money;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import com.mans.ecommerce.b2c.domain.enums.Currency;

public class CartEntityUtill
{

    public void addProduct(Cart cart, ProductInfo productInfo)
    {
        List<ProductInfo> productInfos = cart.getProductInfos();
        productInfos.add(productInfo);
        addProductPriceAndQuantity(cart, productInfo);
    }

    public Optional<ProductInfo> removeProductInfo(
            Cart cart,
            ProductInfoDto productInfoDto)
    {
        Optional<ProductInfo> productInfoOptional = findAndRemoveProduct(cart, productInfoDto);
        if (productInfoOptional.isPresent())
        {
            deductPriceAndQuantity(cart, productInfoOptional.get());
        }
        return productInfoOptional;
    }

    public List<ProductInfo> removeAllProducts(Cart cart)
    {
        List<ProductInfo> productInfos = cart.getProductInfos();
        cart.setProductInfos(new ArrayList<>());
        resetPriceAndQuantity(cart);
        return productInfos;
    }

    private void addProductPriceAndQuantity(Cart cart, ProductInfo productInfo)
    {
        addPrice(cart, productInfo);
        addQuantity(cart, productInfo);
    }

    private void addPrice(Cart cart, ProductInfo productInfo)
    {
        Money productCost = productInfo.getPrice()
                                       .getMoney();
        Money oldCartCost = cart.getMoney();
        Money newCartCost = sum(productCost, oldCartCost);
        cart.setMoney(newCartCost);
    }

    private Money sum(Money productCost, Money cartCost)
    {
        if (productCost.getCurrency() != cartCost.getCurrency())
        {
            throw new UnsupportedOperationException();
        }

        BigDecimal firstAmount = productCost.getAmount();
        BigDecimal secondAmount = cartCost.getAmount();
        BigDecimal sumAmount = firstAmount.add(secondAmount);
        return new Money(sumAmount, cartCost.getCurrency());
    }

    private void addQuantity(Cart cart, ProductInfo productInfo)
    {
        int oldQuantity = cart.getTotalQuantity();
        int newQuantity = oldQuantity + productInfo.getQuantity();
        cart.setTotalQuantity(newQuantity);
    }

    private void resetPriceAndQuantity(Cart cart)
    {
        resetPrice(cart);
        resetQuantity(cart);
    }

    private void resetQuantity(Cart cart)
    {
        cart.setTotalQuantity(0);
    }

    private void resetPrice(Cart cart)
    {
        Currency currency = cart.getMoney().getCurrency();
        Money money = new Money(new BigDecimal("0.00"), currency);
        cart.setMoney(money);
    }

    private void deductPriceAndQuantity(Cart cart, ProductInfo productInfo)
    {
        deductPrice(cart, productInfo);
        deductQuantity(cart, productInfo);
    }

    private void deductPrice(Cart cart, ProductInfo product)
    {
        Money productCost = product.getPrice().getMoney();
        if (productCost.getCurrency() != cart.getMoney().getCurrency())
        {
            throw new UnsupportedOperationException();
        }

        Money newCartMoney = subtract(productCost, cart.getMoney());
        cart.setMoney(newCartMoney);
    }

    private Money subtract(Money productCost, Money cartCost)
    {
        BigDecimal cartAmount = cartCost.getAmount();
        BigDecimal productAmount = productCost.getAmount();
        BigDecimal subtractAmount = cartAmount.subtract(productAmount);
        return new Money(subtractAmount, cartCost.getCurrency());
    }

    private void deductQuantity(Cart cart, ProductInfo product)
    {
        int productQuantity = product.getQuantity();
        int oldCartQuantity = cart.getTotalQuantity();
        int newCartQuantity = oldCartQuantity - productQuantity;

        cart.setTotalQuantity(newCartQuantity);
    }

    private Optional<ProductInfo> findAndRemoveProduct(Cart cart, ProductInfoDto productInfoDto)
    {
        String desireId = productInfoDto.getProductId();
        List<ProductInfo> productInfos = cart.getProductInfos();

        for (int i = 0; i < productInfos.size(); i++)
        {
            ProductInfo currProduct = productInfos.get(i);
            if (desireId.equals(currProduct.getProductId()))
            {
                productInfos.remove(i);
                return Optional.of(currProduct);
            }
        }
        return Optional.empty();
    }

}
