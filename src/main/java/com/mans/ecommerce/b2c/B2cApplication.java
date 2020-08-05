package com.mans.ecommerce.b2c;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

//TODO ProductDetails: List<?> similarItems

@EnableAsync
@SpringBootApplication
public class B2cApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(B2cApplication.class, args);
    }
}
