package com.mans.ecommerce.b2c.controller.customer;

import com.mans.ecommerce.b2c.repository.product.ProductRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class TestController
{
    @Autowired
    ProductRepository productRepository;

    @GetMapping("/get")
    public void get()
    {
//        int looked = productRepository.lock("mans-2", "mans", new ObjectId("2222222222"), 300);
//        System.out.println(looked + "---------------------------------");
//        System.out.println(productRepository.getBySku("mans-2"));
    }

    @GetMapping("/getpr")
    public void g5et()
    {
        System.out.println(productRepository.getBySku("mans-2"));
    }
}
