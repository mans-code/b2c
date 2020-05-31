package com.mans.ecommerce.b2c.controller.utills.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mans.ecommerce.b2c.controller.utills.dto.ProductInfoDto;
import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.Price;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;

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
        Price productPriceDetail = productInfo.getPrice();
        Price cartPriceDetails = cart.getPriceDetails();
        double newCartAmount = productPriceDetail.getAmount() + cartPriceDetails.getAmount();
        cartPriceDetails.setAmount(newCartAmount);
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
        Price cartPriceDetails = cart.getPriceDetails();
        cartPriceDetails.setAmount(0);
    }

    private void deductPriceAndQuantity(Cart cart, ProductInfo productInfo)
    {
        deductPrice(cart, productInfo);
        deductQuantity(cart, productInfo);
    }

    private void deductPrice(Cart cart, ProductInfo product)
    {
        Price productPriceDetails = product.getPrice();
        Price cartPriceDetails = cart.getPriceDetails();

        double productAmount = productPriceDetails.getAmount();
        double oldCartAmount = cartPriceDetails.getAmount();
        double newCartAmount = oldCartAmount - productAmount;

        cartPriceDetails.setAmount(newCartAmount);
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
