package com.mans.ecommerce.b2c.controller.customer;

import com.mans.ecommerce.b2c.domain.entity.customer.Cart;
import com.mans.ecommerce.b2c.domain.entity.customer.Customer;
import com.mans.ecommerce.b2c.domain.exception.OutOfStockException;
import com.mans.ecommerce.b2c.domain.exception.UserAlreadyExistException;
import com.mans.ecommerce.b2c.repository.customer.CustomerRepository;
import com.mans.ecommerce.b2c.repository.product.ProductRepository;
import com.mans.ecommerce.b2c.service.CartService;
import com.mans.ecommerce.b2c.utill.Global;
import com.mongodb.MongoWriteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/test")
public class TestController
{
    @Autowired
    ProductRepository productRepository;

    @Autowired CustomerRepository customerRepository;

    @Autowired CartService cartService;

    @GetMapping("/customer")
    public Mono<String> custome()
    {
        Mono<String> one = Mono.just("one");
        Mono<String> two = Mono.just("two");
        return Mono.zip(one, two).flatMap(tuple -> Mono.just("dddddd"));
    }

    @GetMapping("/cart")
    public Mono<Cart> dmf()
    {
        Cart cart = new Cart();
        cart.setExpireDate(Global.getFuture(10));
        return cartService.create(true);
    }

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
