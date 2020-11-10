package com.mans.ecommerce.b2c.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@Profile("!prod")
public class CorsGlobalConfiguration implements WebFluxConfigurer
{
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                    .allowedOrigins("*")
                    .allowedHeaders("*")
                    .allowedMethods("PUT", "GET", "PATCH", "POST")
                    .maxAge(3600);
    }
}
