package com.mans.ecommerce.b2c.service;

import java.util.List;
import java.util.Objects;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.sharedSubEntity.ProductInfo;
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
            boolean inCart = cartLogic.isInCart(cart, cartProduct);
            if (inCart)
            {
                int toLock = cartProduct.getQuantity();
                return productRepository.partialLock(cartProduct, cartId, toLock);
            }
            return productRepository.lock(cartProduct, cartId);
        });
    }

    public void unlock(Cart cart, List<ProductInfo> productInfos)
    {
        cartService.avoidUnlock(cart)
                   .doOnSuccess(active -> {
                       if (Objects.isNull(active) || !active)
                       {
                            return;
                       }
                       productInfos.forEach(productInfo -> {
                           productRepository.unlock(productInfo, cart.getIdObj());
                       });
                   });

    }

    public void unlock(Cart cart, ProductInfo cartProduct)
    {
        cartService.avoidUnlock(cart)
                   .doOnSuccess(active -> {
                       if (Objects.nonNull(active) && active)
                       {
                           productRepository.unlock(cartProduct, cart.getIdObj());
                       }
                   });

    }

    public void partialUnlock(Cart cart, ProductInfo cartProduct, int toUnlock)
    {
        cartService.avoidUnlock(cart)
                   .doOnSuccess(active -> {
                       if (Objects.nonNull(active) && active)
                       {
                           productRepository.partialUnlock(cartProduct, cart.getIdObj(), toUnlock);
                       }
                   });
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
