package com.mans.ecommerce.b2c.domain.entity.financial.subEntity;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.customer.subEntity.Address;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import com.stripe.model.Charge;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = {})
public class OrderDetail
{
    private OrderHistory OrderHistory;

    private ShippingInfo shippingInfo;

    private PaymentInfo payment;

    private List<ProductInfo> productInfos;

    public OrderDetail(Cart cart, Address address, Charge charge)
    {
        this.shippingInfo = new ShippingInfo(address);
        this.payment = new PaymentInfo(charge);
        this.productInfos = cart.getProductInfos();
    }
}