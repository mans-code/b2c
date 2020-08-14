package com.mans.ecommerce.b2c.security;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Utility class for Spring Security.
 */
@NoArgsConstructor
public final class SecurityUtils {

    public static String getTokenFromRequest(ServerWebExchange serverWebExchange) {
        String token = serverWebExchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);
        return StringUtils.isEmpty(token) ? Strings.EMPTY : token;
    }

}
