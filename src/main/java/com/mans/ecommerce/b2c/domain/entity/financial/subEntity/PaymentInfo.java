package com.mans.ecommerce.b2c.domain.entity.financial.subEntity;

import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.Money;
import com.stripe.model.Charge;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = {})
public class PaymentInfo
{
    private String cardOwnerName;

    private String paymentNum;

    private String lastFourDigits;

    private Money amount;

    private String status;

    public PaymentInfo(Charge charge)
    {
        this.paymentNum = charge.getReceiptNumber();
        this.status = "ordered";
    }
}