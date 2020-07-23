package com.mans.ecommerce.b2c.service;

import java.util.Optional;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.exception.ResourceNotFoundException;
import com.mans.ecommerce.b2c.repository.customer.CartRepository;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class CartService
{

    @Getter
    private final String NOT_FOUND_TEMPLATE = "Couldn't find Cart with id = %s";

    private CartRepository cartRepository;

    CartService(CartRepository cartRepository)
    {
        this.cartRepository = cartRepository;
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

}
