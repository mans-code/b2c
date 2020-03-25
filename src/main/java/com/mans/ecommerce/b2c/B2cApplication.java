package com.mans.ecommerce.b2c;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//TODO ProductInfo: List<String> availableCombination
//TODO ProductGlimpse: String name
//TODO ProductDetails: List<?> similarItems

@SpringBootApplication
public class B2cApplication {

	public static void main(String[] args) {
		SpringApplication.run(B2cApplication.class, args);
	}
	
}
