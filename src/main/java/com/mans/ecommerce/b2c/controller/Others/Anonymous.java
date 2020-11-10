package com.mans.ecommerce.b2c.controller.Others;

import java.util.Optional;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.service.CartService;
import com.mans.ecommerce.b2c.utill.Global;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin
public class Anonymous
{

    private CartService cartService;

    Anonymous(CartService cartService)
    {
        this.cartService = cartService;
    }

    @GetMapping("/anonymous")
    public Mono<Cart> getId(ServerHttpRequest req, ServerHttpResponse res)
    {
        Optional<String> idOpt = Global.getIdHeader(req);
        Mono<Cart> cartMono = idOpt.isPresent() ?
                                      cartService.findById(idOpt.get())
                                                 .switchIfEmpty(cartService.create(true))
                                      : cartService.create(true);
        return cartMono;
    }
}
