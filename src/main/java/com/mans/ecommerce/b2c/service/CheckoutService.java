package com.mans.ecommerce.b2c.service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import com.mans.ecommerce.b2c.domain.functional.CompleteSingle;
import com.mans.ecommerce.b2c.domain.logic.CartLogic;
import com.mans.ecommerce.b2c.repository.product.ProductRepository;
import com.mans.ecommerce.b2c.utill.LockError;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Service
public class CheckoutService
{

    private ProductRepository productRepository;

    private CartService cartService;

    private CartLogic cartLogic;

    CheckoutService(
            ProductRepository productRepository,
            CartService carService,
            CartLogic cartLogic)
    {
        this.productRepository = productRepository;
        this.cartService = carService;
        this.cartLogic = cartLogic;
    }

    public Mono<Tuple2<Cart, List<LockError>>> lock(ObjectId cartId)
    {
        Mono<Cart> cartMono = cartService.findAndLock(cartId);

        Mono<List<LockError>> productLockErrorInfoMono =
                cartMono.flatMapMany(cart -> Flux.<LockError>create(
                        emitter -> getProductsLockErrorInfo(cart, emitter))
                                                     .limitRequest(cart.getProductInfos()
                                                                       .size()))
                        .collectList();

        return cartMono.zipWith(productLockErrorInfoMono);
    }

    public Mono<Integer> lock(Cart cart, ProductInfo cartProduct)
    {
        return cartService.avoidUnlock(cart).flatMap($ -> {
            ObjectId cartId = cart.getId();
            return productRepository.lock(cartProduct, cartId);
        });
    }

    public Mono<Integer> partialLock(Cart cart, ProductInfo cartProduct, int toLock)
    {
        return cartService.avoidUnlock(cart).flatMap($ -> {
            ObjectId cartId = cart.getId();
            return productRepository.partialLock(cartProduct, cartId, toLock);
        });
    }

    public Mono<Boolean> unlock(Cart cart, List<ProductInfo> productInfos)
    {
        return cartService.avoidUnlock(cart).doOnSuccess($ -> {
            productInfos.forEach(productInfo -> {
                productRepository.unlock(productInfo, cart.getId());
            });
        });
    }

    public void unlock(Cart cart, ProductInfo cartProduct)
    {
        cartService.avoidUnlock(cart)
                   .doOnSuccess($ -> {
                       productRepository.unlock(cartProduct, cart.getId());
                   }).subscribe();
    }

    public void partialUnlock(Cart cart, ProductInfo cartProduct, int toUnlock)
    {
        System.err.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        cartService.avoidUnlock(cart)
                   .doOnSuccess(active -> {
                       productRepository.partialUnlock(cartProduct, cart.getId(), toUnlock);
                   }).subscribe();
    }

    private void getProductsLockErrorInfo(Cart cart, FluxSink<LockError> emitter)
    {
        List<ProductInfo> productInfos = cart.getProductInfos();
        int size = productInfos.size();
        AtomicInteger curr = new AtomicInteger(0);
        CompleteSingle completeSingle = completeSingle(size, curr, emitter);

        IntStream.range(0, size)
                 .forEach(index -> {
                     ProductInfo info = productInfos.get(index);
                     getProductsLockErrorInfo(emitter, cart, info, completeSingle);
                 });
    }

    private void getProductsLockErrorInfo(
            FluxSink<LockError> emitter,
            Cart cart,
            ProductInfo info,
            CompleteSingle completeSingle)
    {
        lock(cart, info).doOnSuccess(quantity -> {
            if (info.getQuantity() != quantity)
            {
                info.setQuantity(quantity);
                LockError productLockInfo = getProductLockInfo(info, quantity);
                emitter.next(productLockInfo);
            }
            completeSingle.single();
        }).doOnError($ -> {
            LockError productLockInfo = getProductLockInfo(info, 0);
            emitter.next(productLockInfo);
            completeSingle.single();
        }).subscribe();
    }

    private CompleteSingle completeSingle(int size, AtomicInteger curr, FluxSink<LockError> emitter)
    {
        return () -> {
            if (curr.incrementAndGet() == size)
            {
                emitter.complete();
            }
        };
    }

    private LockError getProductLockInfo(ProductInfo productInfo, int lockedQuantity)
    {
        return LockError
                       .builder()
                       .sku(productInfo.getSku())
                       .variationId(productInfo.getVariationId())
                       .title(productInfo.getTitle())
                       .lockedQuantity(lockedQuantity)
                       .requestedQuantity(productInfo.getQuantity())
                       .build();
    }

}
