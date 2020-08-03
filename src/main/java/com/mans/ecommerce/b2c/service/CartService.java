package com.mans.ecommerce.b2c.service;

import java.util.Date;
import java.util.Optional;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.exception.ConflictException;
import com.mans.ecommerce.b2c.domain.exception.ResourceNotFoundException;
import com.mans.ecommerce.b2c.repository.customer.CartRepository;
import com.mans.ecommerce.b2c.utill.Global;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CartService
{

    @Getter
    private final String NOT_FOUND_TEMPLATE = "Couldn't find Cart with id = %s";

    private final int validityInMinutes;

    private CartRepository cartRepository;

    CartService(
            CartRepository cartRepository,
            @Value("${app.cart.expiration}") int validityInMinutes)
    {
        this.cartRepository = cartRepository;
        this.validityInMinutes = validityInMinutes;
    }

    public Cart findById(String id)
    {
        Optional<Cart> optionalCart = cartRepository.findById(id);

        if (!optionalCart.isPresent())
        {
            throw new ResourceNotFoundException(String.format(NOT_FOUND_TEMPLATE, id));
        }

        return optionalCart.get();
    }

    public Cart save(Cart cart)
    {
        return cartRepository.save(cart);
    }

    public Cart activateAndSave(Cart cart)
    {
        cart.setActive(true);
        Date date = Global.getFuture(validityInMinutes);
        cart.setExpireDate(date);
        return save(cart);
    }

    public void avoidUnlock(Cart cart)
    {
        if (cart.isActive() && expireIn10Mins(cart.getExpireDate()))
        {
            boolean stillActive = extendsExpirationDate(cart);
            if (!stillActive)
            {
                throw new ConflictException("please start checkout procedure again");
            }
        }
    }

    private boolean expireIn10Mins(Date date)
    {
        int TEN_MINUTES = 10;
        int MINUS_TEN = -10;
        Date after10Mins = Global.getFuture(TEN_MINUTES);
        long diff = date.getTime() - after10Mins.getTime();
        int diffmin = (int) (diff / (60 * 1000));

        if (diffmin > 0 || diffmin >= MINUS_TEN)
        {
            return true;
        }
        return false;
    }

    private boolean extendsExpirationDate(Cart cart)
    {
        String id = cart.getId();
        Date date = Global.getFuture(validityInMinutes / 2);
        return cartRepository.extendsExpirationDate(id, date);
    }

}
