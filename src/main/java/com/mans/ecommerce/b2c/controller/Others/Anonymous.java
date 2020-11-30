package com.mans.ecommerce.b2c.controller.Others;

import java.util.Optional;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.customer.subEntity.Address;
import com.mans.ecommerce.b2c.service.CartService;
import com.mans.ecommerce.b2c.utill.Global;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Tag(name = "anonymous api", description = "deals with unknown customers")
public class Anonymous
{

    private CartService cartService;

    Anonymous(CartService cartService)
    {
        this.cartService = cartService;
    }

    @GetMapping("/anonymous")
    @Operation(description = "assign an id to new customer and create a cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Cart.class))),
    })
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
