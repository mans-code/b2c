package com.mans.ecommerce.b2c.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;

@Configuration
public class MyWebApplicationInitializer
        implements WebApplicationInitializer
{

    @Override
    public void onStartup(ServletContext servletContext)
            throws ServletException
    {
        servletContext.setInitParameter(
                "spring.profiles.active", "dev");
    }
}