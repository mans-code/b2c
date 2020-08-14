package com.mans.ecommerce.b2c;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.reactive.config.EnableWebFlux;

//TODO ProductDetails: List<?> similarItems

@EnableAsync
@SpringBootApplication(exclude = ErrorWebFluxAutoConfiguration.class)
public class B2cApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(B2cApplication.class, args);
    }
}
