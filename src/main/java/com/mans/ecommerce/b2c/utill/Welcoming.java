package com.mans.ecommerce.b2c.utill;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class Welcoming extends HandlerInterceptorAdapter
{
    @Override public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex)
            throws Exception
    {
        super.afterCompletion(request, response, handler, ex);
    }
}
