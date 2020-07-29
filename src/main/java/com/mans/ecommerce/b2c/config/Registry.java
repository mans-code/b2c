package com.mans.ecommerce.b2c.config;

import com.mans.ecommerce.b2c.utill.Welcoming;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Registry implements WebMvcConfigurer
{
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        String signupUrl = "/auth/signup";
        registry.addInterceptor(new Welcoming()).addPathPatterns(signupUrl);
    }

    @Override
    public void addFormatters(FormatterRegistry registry)
    {
        registry.addConverter(new CustomConverter.StringToSortConverter());
    }
}
