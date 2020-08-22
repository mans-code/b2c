package com.mans.ecommerce.b2c.service;

import java.time.Instant;
import java.time.LocalDateTime;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.exception.ConflictException;
import com.mans.ecommerce.b2c.domain.exception.ResourceNotFoundException;
import com.mans.ecommerce.b2c.repository.customer.CartRepository;
import com.mans.ecommerce.b2c.utill.Global;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
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

    private FeedService feedService;

    CartService(
            CartRepository cartRepository,
            FeedService feedService,
            @Value("${app.cart.expiration}") int validityInMinutes)
    {
        this.cartRepository = cartRepository;
        this.feedService = feedService;
        this.validityInMinutes = validityInMinutes;
    }

    public Mono<Cart> findById(ObjectId id)
    {
        return cartRepository.findById(id)
                             .switchIfEmpty(Mono.defer(this::raiseCartNotFound));

    }

    public Mono<Cart> findById(String id)
    {
        return findById(new ObjectId(id));
    }

    public Mono<Cart> findAndLock(ObjectId id)
    {
        Instant time = Global.getFuture(validityInMinutes);
        return cartRepository.findAndLock(id, time)
                             .switchIfEmpty(Mono.defer(this::raiseCartNotFound));
    }

    public Mono<Cart> update(Cart cart)
    {
        return cartRepository.save(cart);
    }

    public Mono<Cart> create(boolean anonymous)
    {
        Cart cart = new Cart(anonymous);
        return cartRepository.save(cart)
                             .switchIfEmpty(Mono.defer(this::raiseDBException))
                             .doOnSuccess(saved -> {
                                 createFeedEvent(saved);
                             });
    }

    public Mono<Boolean> avoidUnlock(Cart cart)
    {
        if (cart.isActive() && expireIn10Mins(cart.getExpireDate()))
        {
            Mono<Boolean> stillActive = extendsExpirationDateAndGetActivationStatus(cart.getId());
            return stillActive.doOnSuccess(this::throwIfNotActive);

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
        Instant date = Global.getFuture(validityInMinutes / 2);
        return cartRepository.extendsExpirationDateAndGetActivationStatus(cartId, date);
    }

    private void createFeedEvent(Cart cart)
    {
        ObjectId id = cart.getId();
        feedService.save(id);
    }

    private boolean expireIn10Mins(Instant time)
    {
        LocalDateTime.from(time);
        int TEN_MINUTES = 10;
        long diffMins = MINUTES.between(time, LocalDateTime.now());
        if (diffMins <= TEN_MINUTES)
        {
            return true;
        }
        return false;
    }

    private Mono<? extends Cart> throwResourceNotFound(ObjectId id)
    {
        return Mono.error(new ResourceNotFoundException(String.format(NOT_FOUND_TEMPLATE, id)));
    }

    private <T> Mono<T> raiseDBException()
    {
        return Mono.error(new ResourceNotFoundException("could Not save cart"));
    }

    private <T> Mono<T> raiseCartNotFound()
    {
        return Mono.error(new ResourceNotFoundException("Cart Not Found"));
    }
}
