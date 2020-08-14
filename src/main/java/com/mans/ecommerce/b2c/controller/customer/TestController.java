package com.mans.ecommerce.b2c.controller.customer;

import com.mans.ecommerce.b2c.domain.exception.OutOfStockException;
import com.mans.ecommerce.b2c.repository.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/test")
public class TestController
{
    @Autowired
    ProductRepository productRepository;

    @GetMapping("/get")
    public Mono<String> get()
    {
        return Mono.error(new OutOfStockException());
    }

    @GetMapping("/getpr")
    public void g5et()
    {
        System.out.println(productRepository.getBySku("mans-2"));
    }
}
