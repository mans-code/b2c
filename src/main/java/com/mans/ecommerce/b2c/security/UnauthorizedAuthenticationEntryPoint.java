package com.mans.ecommerce.b2c.security;

import com.mans.ecommerce.b2c.domain.exception.UnauthorizedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class UnauthorizedAuthenticationEntryPoint implements ServerAuthenticationEntryPoint
{
    @Override
    public Mono<Void> commence(final ServerWebExchange exchange, final AuthenticationException e)
    {
        return Mono.error(new UnauthorizedException());
    }
}
