package com.mans.ecommerce.b2c.service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService
{

    @Value("${app.stripe.secret.key}")
    private String secretKey;

    @PostConstruct
    public void init()
    {
        Stripe.apiKey = secretKey;
    }

    public Charge chargeNewCard(String token, double amount, String currency)
            throws StripeException
    {
        Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("amount", (int)(amount * 100));
        chargeParams.put("currency", currency);
        chargeParams.put("source", token);
        Charge charge = Charge.create(chargeParams);
        return charge;
    }
}