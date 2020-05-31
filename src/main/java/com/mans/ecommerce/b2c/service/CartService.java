package com.mans.ecommerce.b2c.service;

import java.util.Optional;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.exception.ResourceNotFoundException;
import com.mans.ecommerce.b2c.repository.customer.CartRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService
{
    private CartRepository cartRepository;

    CartService(CartRepository cartRepository)
    {
        this.cartRepository = cartRepository;
    }

    public Optional<Cart> findById(String id)
    {
        return cartRepository.findById(id);
    }

    public Cart findByIdOrElseThrow(String id)
    {
        Optional<Cart> optionalCart = findById(id);

        if (!optionalCart.isPresent())
        {
            throw new ResourceNotFoundException(String.format("Couldn't find Cart with id = %s", id));
        }

        return optionalCart.get();
    }

    public Cart save(Cart cart)
    {
        return cartRepository.save(cart);
    }

}
