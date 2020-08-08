package com.mans.ecommerce.b2c.server.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.repository.customer.CartRepository;
import com.mans.ecommerce.b2c.service.CartService;
import com.mans.ecommerce.b2c.service.ProductService;
import com.mans.ecommerce.b2c.utill.Global;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
@WebFilter(urlPatterns = "/carts/anonymous")
public class AnonymousCart extends GenericFilterBean
{

    private final String ANONYMOUS = "anonymous";

    private final String CUSTOMER_ID = "customerId";

    private CartService cartService;

    private ProductService productService;

    private CartRepository cartRepository;

    AnonymousCart(CartService cartService, ProductService productService, CartRepository cartRepository)
    {
        this.productService = productService;
        this.cartService = cartService;
        this.cartRepository = cartRepository;
    }

    @Override public void doFilter(
            ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException
    {
        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpServletResponse httRes = (HttpServletResponse) res;

        String requestURI = httpReq.getRequestURI();

        if (requestURI.contains(ANONYMOUS))
        {
            String customerId = getCustomerId(httpReq, httRes);
            String newURI = requestURI.replace(ANONYMOUS, customerId);
            RequestDispatcher dispatcher = getServletContext()
                                                   .getRequestDispatcher(newURI);

            dispatcher.forward(httpReq, httRes);
        }
        else
        {
            chain.doFilter(req, res);
        }
    }

    private String getCustomerId(HttpServletRequest request, HttpServletResponse response)
    {
        Optional<Cookie> cookieOptional = Global.getCookie(request, CUSTOMER_ID);
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

    private void addCookie(String customerId, HttpServletResponse response)
    {
        Cookie cookie = new Cookie(CUSTOMER_ID, customerId);
        response.addCookie(cookie);
    }
}
