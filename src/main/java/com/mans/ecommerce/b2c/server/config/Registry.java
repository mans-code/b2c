package com.mans.ecommerce.b2c.server.config;

import com.mans.ecommerce.b2c.server.Interceptor.AnonymousCartInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Registry implements WebMvcConfigurer
{
    @Override
    public void addFormatters(FormatterRegistry registry)
    {
        registry.addConverter(new CustomConverter.StringToSortConverter());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(new AnonymousCartInterceptor()).addPathPatterns("/carts/anonymous", "/carts/anonymous/**");
    }
}
