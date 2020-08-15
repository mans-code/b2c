package com.mans.ecommerce.b2c.service;

import java.util.List;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
import com.mans.ecommerce.b2c.repository.product.ProductRepository;
import com.mans.ecommerce.b2c.utill.LockError;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationEventPublisher;
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

    private ApplicationEventPublisher publisher;

    CheckoutService(
            ProductRepository productRepository,
            CartService carService,
            ApplicationEventPublisher publisher)
    {
        this.productRepository = productRepository;
        this.cartService = carService;
        this.publisher = publisher;
    }

    public Mono<Tuple2<Cart, List<LockError>>> lock(ObjectId cartId)
    {
        Mono<Cart> cartMono = cartService.findAndLock(cartId);
        Mono<List<LockError>> productLockErrorInfoMono =
                cartMono.flatMapMany(cart ->
                                             Flux.<LockError>create(
                                                     emitter -> getProductsLockErrorInfo(cart, emitter)))
                        .collectList();

        return cartMono.zipWith(productLockErrorInfoMono);
    }

    public Mono<Integer> lock(Cart cart, ProductInfo cartProduct)
    {
        return cartService.avoidUnlock(cart).flatMap(x -> {
            ObjectId cartId = cart.getIdObj();
            return productRepository.lock(cartProduct, cartId);
        });
    }

    public Mono<Integer> lock(Cart cart, ProductInfo cartProduct, int toLock)
    {
        return cartService.avoidUnlock(cart).flatMap(x -> {
            ObjectId cartId = cart.getIdObj();
            return productRepository.partialLock(cartProduct, cartId, toLock);
        });
    }

    public void unlock(ObjectId cartId, List<ProductInfo> productInfos)
    {
        productInfos.forEach(productInfo -> {
            productRepository.unlock(productInfo, cartId);
        });
    }

    public void unlock(ObjectId cartId, ProductInfo cartProduct)
    {
        productRepository.unlock(cartProduct, cartId);
    }

    public void unlock(ObjectId cartId, ProductInfo cartProduct, int toUnlock)
    {
        productRepository.partialUnlock(cartProduct, cartId, toUnlock);
    }

    private void getProductsLockErrorInfo(Cart cart, FluxSink<LockError> emitter)
    {
        for (ProductInfo productInfo : cart.getProductInfos())
        {
            Mono<Integer> lockedQuantityMono = lock(cart, productInfo);
            lockedQuantityMono.doOnSuccess(quantity -> {
                if (productInfo.getQuantity() != quantity)
                {
                    productInfo.setQuantity(quantity);
                    LockError productLockInfo = getProductLockInfo(productInfo, quantity);
                    emitter.next(productLockInfo);
                }
                emitter.complete();
            });
        }
    }

    private LockError getProductLockInfo(ProductInfo productInfo, int lockedQuantity)
    {
        return LockError
                       .builder()
                       .sku(productInfo.getSku())
                       .title(productInfo.getTitle())
                       .lockedQuantity(lockedQuantity)
                       .requestedQuantity(productInfo.getQuantity())
                       .build();
    }

}
