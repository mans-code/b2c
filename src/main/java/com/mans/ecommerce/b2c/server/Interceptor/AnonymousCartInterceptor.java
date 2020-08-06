package com.mans.ecommerce.b2c.server.Interceptor;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.service.CartService;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class AnonymousCartInterceptor extends HandlerInterceptorAdapter
{

    private final String ANONYMOUS = "anonymous";

    private final String CART_ID = "cartId";

    private CartService cartService;

    AnonymousCartInterceptor(CartService cartService)
    {
        this.cartService = cartService;
    }

    @Override public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception
    {
        String cartId = getCartId(request, response);
        String forward = request.getRequestURI().replace(ANONYMOUS, cartId);
        RequestDispatcher dispatcher = request.getServletContext()
                                              .getRequestDispatcher(forward);
        dispatcher.forward(request, response);
        return false;
    }

    private String getCartId(HttpServletRequest request, HttpServletResponse response)
    {
        Optional<Cookie> cookieOptional = getCookie(CART_ID, request);
        if (cookieOptional.isPresent())
        {
            return cookieOptional.get().getValue();
        }
        return createCookieAndGetCartId(response);
    }

    private String createCookieAndGetCartId(HttpServletResponse response)
    {
        String customerId = cartService
                                    .syncSave(new Cart())
                                    .getId();

        addCookie(customerId, response);
        return customerId;
    }

    private Optional<Cookie> getCookie(String customer_id, HttpServletRequest request)
    {
        return Arrays.stream(request.getCookies())
                     .filter(cookie -> cookie.getName().equalsIgnoreCase(CART_ID))
                     .findFirst();
    }

    private void addCookie(String customerId, HttpServletResponse response)
    {
        Cookie cookie = new Cookie(CART_ID, customerId);
        response.addCookie(cookie);
    }
}
