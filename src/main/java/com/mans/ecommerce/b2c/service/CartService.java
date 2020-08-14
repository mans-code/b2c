package com.mans.ecommerce.b2c.service;

import java.time.LocalTime;
import java.util.Date;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.exception.ConflictException;
import com.mans.ecommerce.b2c.domain.exception.DBException;
import com.mans.ecommerce.b2c.domain.exception.ResourceNotFoundException;
import com.mans.ecommerce.b2c.repository.customer.CartRepository;
import com.mans.ecommerce.b2c.server.eventListener.entity.CreateFeedEvent;
import com.mans.ecommerce.b2c.utill.Global;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import static java.time.temporal.ChronoUnit.MINUTES;

@Service
public class CartService
{

    @Getter
    private final String NOT_FOUND_TEMPLATE = "Couldn't find Cart with id = %s";

    private final int validityInMinutes;

    private CartRepository cartRepository;

    private ApplicationEventPublisher publisher;

    CartService(
            CartRepository cartRepository,
            @Value("${app.cart.expiration}") int validityInMinutes)
    {
        this.cartRepository = cartRepository;
        this.validityInMinutes = validityInMinutes;
    }

    public Mono<Cart> findById(ObjectId id)
    {

        Mono<Cart> cartMono = cartRepository.findById(id);
        cartMono.doOnSuccess(cart -> throwIfNull(cart));
        return cartMono;
    }

    public Mono<Cart> findAndLock(ObjectId id)
    {
        Date date = Global.getFuture(validityInMinutes);
        Mono<Cart> cartMono = cartRepository.findAndLock(id, date);
        cartMono.doOnSuccess(cart -> throwIfNull(cart));
        return cartMono;
    }

    private Mono<Object> throwIfNull(Cart cart)
    {
        if (cart == null)
        {
            return Mono.error(new ResourceNotFoundException(String.format(NOT_FOUND_TEMPLATE, cart.getId())));
        }
        return null;
    }

    public Mono<Cart> update(Cart cart)
    {
        Mono<Cart> cartMono = cartRepository.save(cart);
        cartMono.doOnError(ex -> {
            Mono.error(new DBException(String.format("can't save cart id=%s", cart.getId())));
        });
        return cartMono;
    }

    public Mono<Cart> create(boolean anonymous)
    {
        Cart cart = new Cart(anonymous);
        Mono<Cart> saved = cartRepository.save(cart);
        createFeedEvent(saved);
        return saved;
    }

    public Mono<Boolean> avoidUnlock(Cart cart)
    {
        if (cart.isActive() && expireIn10Mins(cart.getExpireDate()))
        {
            Mono<Boolean> stillActive = extendsExpirationDateAndGetActivationStatus(cart.getIdObj());
            return stillActive.doOnSuccess(active -> throwIfNotActive(active));

        }
        return Mono.just(true);
    }

    private void throwIfNotActive(boolean stillActive)
    {
        if (!stillActive)
        {
            throw new ConflictException("please start checkout procedure again");
        }
    }

    public Mono<Boolean> extendsExpirationDateAndGetActivationStatus(ObjectId cartId)
    {
        Date date = Global.getFuture(validityInMinutes / 2);
        return cartRepository.extendsExpirationDateAndGetActivationStatus(cartId, date);
    }

    private void createFeedEvent(Mono<Cart> cart)
    {
        cart.doOnSuccess(savedCart -> {
            if (savedCart != null)
            {
                CreateFeedEvent feedEvent = new CreateFeedEvent(savedCart.getIdObj());
                publisher.publishEvent(feedEvent);
            }
        });
    }

    private boolean expireIn10Mins(LocalTime time)
    {
        int TEN_MINUTES = 10;
        long diffMins = MINUTES.between(time, LocalTime.now());
        if (diffMins <= TEN_MINUTES)
        {
            return true;
        }
        return false;
    }
}
